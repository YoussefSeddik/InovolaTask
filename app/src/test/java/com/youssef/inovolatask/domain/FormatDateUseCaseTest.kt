package com.youssef.inovolatask.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class FormatDateUseCaseTest {

    private val useCase = FormatDateUseCase()

    @Test
    fun `formats timestamp correctly`() {
        val calendar = Calendar.getInstance().apply {
            set(2025, Calendar.AUGUST, 7, 18, 0)
        }
        val timestamp = calendar.timeInMillis
        val expected = "07 Aug 2025, 06:00 PM"
        val result = useCase(timestamp)
        assertEquals(expected, result)
    }
}
