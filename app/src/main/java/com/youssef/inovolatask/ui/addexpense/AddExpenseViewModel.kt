package com.youssef.inovolatask.ui.addexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youssef.inovolatask.domain.AddExpenseUseCase
import com.youssef.inovolatask.domain.ConvertToUSDUseCase
import com.youssef.inovolatask.domain.ValidateAmountInputUseCase
import com.youssef.inovolatask.domain.data.Expense
import com.youssef.inovolatask.domain.data.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface AddExpenseUiState {
    data object Idle : AddExpenseUiState
    data object Converting : AddExpenseUiState
    data object Saving : AddExpenseUiState
    data object Saved : AddExpenseUiState
    data class Error(val message: String) : AddExpenseUiState
}

class AddExpenseViewModel(
    private val validateAmountInputUseCase: ValidateAmountInputUseCase,
    private val convertToUSDUseCase: ConvertToUSDUseCase,
    private val addExpenseUseCase: AddExpenseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddExpenseUiState>(AddExpenseUiState.Idle)
    val uiState: StateFlow<AddExpenseUiState> = _uiState

    fun validateAmountInput(input: String): ValidationResult {
        return validateAmountInputUseCase(input)
    }

    fun saveExpense(input: String, expense: Expense) {
        viewModelScope.launch {
            try {
                _uiState.value = AddExpenseUiState.Converting
                val convertedCurrency = convertToUSDUseCase(input)
                if (convertedCurrency.amount == 0.0 && convertedCurrency.convertedAmount == 0.0) {
                    _uiState.value = AddExpenseUiState.Error("Invalid amount")
                    return@launch
                }
                _uiState.value = AddExpenseUiState.Saving
                addExpenseUseCase(
                    expense.copy(
                        amount = convertedCurrency.amount,
                        convertedAmount = convertedCurrency.convertedAmount,
                        currency = convertedCurrency.currency
                    )
                )
                _uiState.value = AddExpenseUiState.Saved
            } catch (e: Exception) {
                _uiState.value = AddExpenseUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = AddExpenseUiState.Idle
    }
}
