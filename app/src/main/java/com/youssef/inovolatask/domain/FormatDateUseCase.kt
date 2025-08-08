package com.youssef.inovolatask.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormatDateUseCase {
    operator fun invoke(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return format.format(date)
    }
}