package com.youssef.inovolatask.ui.addexpense

import android.app.DatePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.youssef.inovolatask.domain.data.Expense
import com.youssef.inovolatask.ui.dashboard.REFRESH_DASHBOARD
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val categories = listOf("Groceries", "Bills", "Transport", "Food", "Health")
    var category by remember { mutableStateOf(categories.first()) }
    var input by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(System.currentTimeMillis()) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showCategorySheet by remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }
    val validation = viewModel.validateAmountInput(input)
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val pickingImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    LaunchedEffect(uiState) {
        when (uiState) {
            is AddExpenseUiState.Saved -> {
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(REFRESH_DASHBOARD, true)
                navController.popBackStack()
            }

            is AddExpenseUiState.Error -> {
                Toast.makeText(
                    context,
                    (uiState as AddExpenseUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    if (showDatePicker.value) {
        val cal = Calendar.getInstance().apply { timeInMillis = date }
        DatePickerDialog(
            context,
            { _, y, m, d ->
                val c = Calendar.getInstance()
                c.set(y, m, d)
                date = c.timeInMillis
                showDatePicker.value = false
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Add Expense")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text("Category")
                Text(
                    text = category,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCategorySheet = true }
                        .padding(12.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Amount (e.g., 100 EGP)") },
                    isError = input.isNotBlank() && !validation.isValid,
                    supportingText = {
                        if (input.isNotBlank() && !validation.isValid) {
                            Text(validation.errorMessage ?: "")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is AddExpenseUiState.Saving &&
                            uiState !is AddExpenseUiState.Converting
                )

                Spacer(Modifier.height(8.dp))

                Text("Date")
                Text(
                    text = dateFormatter.format(Date(date)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker.value = true }
                        .padding(12.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = { pickingImage.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is AddExpenseUiState.Saving &&
                            uiState !is AddExpenseUiState.Converting
                ) {
                    Text("Attach Receipt")
                }

                imageUri?.let { uri ->
                    Spacer(Modifier.height(8.dp))
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!validation.isValid) {
                            Toast.makeText(
                                context,
                                validation.errorMessage ?: "Invalid input",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        viewModel.saveExpense(
                            input,
                            Expense(
                                category = category,
                                date = date,
                                receiptUri = imageUri?.toString()
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is AddExpenseUiState.Saving &&
                            uiState !is AddExpenseUiState.Converting
                ) {
                    Text(
                        when (uiState) {
                            AddExpenseUiState.Converting -> "Converting..."
                            AddExpenseUiState.Saving -> "Saving..."
                            else -> "Save Expense"
                        }
                    )
                }
            }

            if (uiState is AddExpenseUiState.Converting ||
                uiState is AddExpenseUiState.Saving
            ) {
                Box(
                    Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    if (showCategorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showCategorySheet = false }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Categories", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                categories.forEach { cat ->
                    Text(
                        text = cat,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                category = cat
                                showCategorySheet = false
                            }
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
