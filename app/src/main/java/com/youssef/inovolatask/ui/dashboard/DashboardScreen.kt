package com.youssef.inovolatask.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.youssef.inovolatask.R
import com.youssef.inovolatask.domain.data.Expense
import com.youssef.inovolatask.domain.data.ExpenseFilter
import com.youssef.inovolatask.util.format

const val REFRESH_DASHBOARD = "REFRESH_DASHBOARD"

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val totalExpenses by viewModel.totalExpenses.collectAsStateWithLifecycle()
    val totalIncome = viewModel.totalIncome
    val totalBalance by viewModel.totalBalance.collectAsStateWithLifecycle()
    val expenses by viewModel.filteredExpenses.collectAsStateWithLifecycle()
    val refreshDashboard = navController.currentBackStackEntry?.savedStateHandle?.getStateFlow(
        REFRESH_DASHBOARD,
        false
    )?.collectAsState()

    LaunchedEffect(refreshDashboard?.value) {
        if (refreshDashboard?.value == true) {
            viewModel.refresh()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set(REFRESH_DASHBOARD, false)
        }
    }
    val handleExport = {
        val file = viewModel.exportCsv(expenses)
        if (file != null) viewModel.shareCsvFile(file)
        else Toast.makeText(context, "Failed to export CSV", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        floatingActionButton = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                FloatingActionButton(
                    onClick = { navController.navigate("add") },
                    containerColor = MaterialTheme.colorScheme.primary
                ) { Text("+", color = Color.White) }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            HeaderSection(
                selected = selectedFilter,
                onFilterChanged = viewModel::updateFilter,
                onExportClick = handleExport,
                onRetry = viewModel::refresh
            )
            BalanceCard(
                totalExpenses = totalExpenses ?: 0.0,
                totalIncome = totalIncome,
                totalBalance = totalBalance,
                modifier = Modifier.offset(y = (-90).dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Recent Expenses",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-90).dp)
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-90).dp)
            ) {
                when (val s = uiState) {
                    is DashboardViewModel.DashboardUiState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is DashboardViewModel.DashboardUiState.Empty -> {
                        EmptyState(onAdd = { navController.navigate("add") })
                    }

                    is DashboardViewModel.DashboardUiState.Error -> {
                        ErrorState(
                            message = s.message,
                            onRetry = viewModel::refresh
                        )
                    }

                    is DashboardViewModel.DashboardUiState.Success -> {
                        LazyColumn(Modifier.fillMaxSize()) {
                            items(s.expenses) { expense ->
                                ExpenseItem(expense) { viewModel.formatTime(it) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    selected: ExpenseFilter,
    onFilterChanged: (ExpenseFilter) -> Unit,
    onExportClick: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Profile",
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text("Good Morning", color = Color.White)
                Text("Shihab Rahman", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.weight(1f))
            FilterDropdown(selected, onFilterChanged)
            IconButton(onClick = onExportClick) {
                Icon(Icons.Default.Share, contentDescription = "Export", tint = Color.White)
            }
            IconButton(onClick = onRetry) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
            }
        }
    }
}

@Composable
private fun FilterDropdown(
    selected: ExpenseFilter,
    onFilterChanged: (ExpenseFilter) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(horizontal = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    text = when (selected) {
                        ExpenseFilter.THIS_MONTH -> "This Month"
                        ExpenseFilter.LAST_7_DAYS -> "Last 7 Days"
                    },
                    color = Color.Black,
                    style = MaterialTheme.typography.labelSmall
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("This Month", style = MaterialTheme.typography.labelSmall) },
                onClick = { onFilterChanged(ExpenseFilter.THIS_MONTH); expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Last 7 Days", style = MaterialTheme.typography.labelSmall) },
                onClick = { onFilterChanged(ExpenseFilter.LAST_7_DAYS); expanded = false }
            )
        }
    }
}

@Composable
fun BalanceCard(
    totalExpenses: Double,
    totalIncome: Double,
    totalBalance: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .offset(y = (-20).dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text("Total Balance", color = Color.White)
            Spacer(Modifier.height(4.dp))
            Text(
                "$${totalBalance.format()}",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            Row {
                Column(Modifier.weight(1f)) {
                    Text("Income", color = Color.White.copy(alpha = 0.7f))
                    Text("$${totalIncome.format()}", color = Color.White)
                }
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text("Expenses", color = Color.White.copy(alpha = 0.7f))
                    Text("$${totalExpenses.format()}", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense, formatTime: (Long) -> String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(expense.category, fontWeight = FontWeight.Bold)
                Text("Manually")
            }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "-$${expense.convertedAmount}",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Text(formatTime(expense.date))
            }
        }
    }
}

@Composable
private fun EmptyState(onAdd: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No expenses yet")
        Spacer(Modifier.height(8.dp))
        Button(onClick = onAdd) { Text("Add Expense") }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error: $message", color = Color.Red)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}


