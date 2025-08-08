package com.youssef.inovolatask.domain

import android.content.Context
import android.os.Environment
import com.youssef.inovolatask.domain.data.Expense
import java.io.File
import java.io.FileWriter

class ExportExpensesToCsvUseCase(
    private val context: Context,
    private val formatDateUseCase: FormatDateUseCase,
) {

    operator fun invoke(expenses: List<Expense>): File? {
        return try {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                "expenses.csv"
            )

            FileWriter(file).use { writer ->
                writer.append("Category,Amount,ConvertedAmount,Date\n")
                expenses.forEach {
                    writer.append(
                        "${it.category},${it.amount} ${it.currency},${it.convertedAmount} USD,${
                            formatDateUseCase.invoke(it.date)
                        }\n"
                    )
                }
            }

            file
        } catch (e: Exception) {
            null
        }
    }
}
