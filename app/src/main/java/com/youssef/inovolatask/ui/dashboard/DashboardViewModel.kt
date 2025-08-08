package com.youssef.inovolatask.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youssef.inovolatask.domain.ExportExpensesToCsvUseCase
import com.youssef.inovolatask.domain.FilterExpensesUseCase
import com.youssef.inovolatask.domain.FormatDateUseCase
import com.youssef.inovolatask.domain.GetPaginatedExpensesUseCase
import com.youssef.inovolatask.domain.GetTotalExpensesUseCase
import com.youssef.inovolatask.domain.ShareFileUseCase
import com.youssef.inovolatask.domain.data.Expense
import com.youssef.inovolatask.domain.data.ExpenseFilter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

class DashboardViewModel(
    private val getPaginatedExpensesUseCase: GetPaginatedExpensesUseCase,
    private val getTotalExpensesUseCase: GetTotalExpensesUseCase,
    private val formatDateUseCase: FormatDateUseCase,
    private val filterExpensesUseCase: FilterExpensesUseCase,
    private val exportExpensesToCsvUseCase: ExportExpensesToCsvUseCase,
    private val shareFileUseCase: ShareFileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(
        DashboardUiState.Loading
    )
    val uiState: StateFlow<DashboardUiState> = _uiState

    private val expenses = MutableStateFlow<List<Expense>>(emptyList())
    private val filter = MutableStateFlow(ExpenseFilter.THIS_MONTH)
    val selectedFilter = filter.asStateFlow()

    val filteredExpenses: StateFlow<List<Expense>> =
        combine(expenses, filter) { list, f ->
            filterExpensesUseCase(list, f)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private var currentOffset = 0
    private val limit = 20
    private var isLoading = false
    private var firstLoadDone = false

    val totalIncome = 500_000.0

    val totalExpenses: StateFlow<Double?> =
        getTotalExpensesUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val totalBalance: StateFlow<Double> =
        totalExpenses.map { expenses -> totalIncome - (expenses ?: 0.0) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    init {
        observeFilteredForUi()
        refresh()
    }

    fun updateFilter(newFilter: ExpenseFilter) {
        filter.value = newFilter
        refresh()
    }

    fun refresh() {
        currentOffset = 0
        firstLoadDone = false
        expenses.value = emptyList()
        _uiState.value = DashboardUiState.Loading
        loadNextPage()
    }

    private fun loadNextPage() {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            try {
                getPaginatedExpensesUseCase(limit, currentOffset).collect { newItems ->
                    currentOffset += newItems.size
                    expenses.value = expenses.value + newItems
                    firstLoadDone = true
                    isLoading = false
                    updateUiStateFromCurrent()
                }
            } catch (e: Exception) {
                isLoading = false
                if (!firstLoadDone) {
                    _uiState.value = DashboardUiState.Error(
                        e.message ?: "Something went wrong"
                    )
                } else {
                    _uiState.value = DashboardUiState.Success(
                        filteredExpenses.value
                    )
                }
            }
        }
    }

    private fun observeFilteredForUi() {
        viewModelScope.launch {
            filteredExpenses.collect { list ->
                if (!firstLoadDone) return@collect
                _uiState.value = if (list.isEmpty()) {
                    DashboardUiState.Empty
                } else {
                    DashboardUiState.Success(list)
                }
            }
        }
    }

    private fun updateUiStateFromCurrent() {
        val list = filteredExpenses.value
        _uiState.value = if (list.isEmpty()) {
            if (firstLoadDone) DashboardUiState.Empty
            else DashboardUiState.Loading
        } else {
            DashboardUiState.Success(list)
        }
    }

    fun formatTime(timestamp: Long): String = formatDateUseCase(timestamp)

    fun shareCsvFile(file: File) { shareFileUseCase(file) }

    fun exportCsv(list: List<Expense>): File? =
        exportExpensesToCsvUseCase(list)

    sealed interface DashboardUiState {
        data object Loading : DashboardUiState
        data class Success(val expenses: List<Expense>) : DashboardUiState
        data object Empty : DashboardUiState
        data class Error(val message: String) : DashboardUiState
    }
}

