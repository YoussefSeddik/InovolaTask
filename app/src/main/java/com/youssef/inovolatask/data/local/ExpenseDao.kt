package com.youssef.inovolatask.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getExpenses(limit: Int, offset: Int): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(convertedAmount) FROM expenses")
    fun getExpenses(): Flow<Double?>
}