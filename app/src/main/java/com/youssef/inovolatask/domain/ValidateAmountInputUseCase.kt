package com.youssef.inovolatask.domain

import com.youssef.inovolatask.domain.data.ValidationResult

class ValidateAmountInputUseCase {
    operator fun invoke(input: String): ValidationResult {
        val regex = Regex("^\\d+(\\.\\d{1,2})?\\s+[A-Za-z]{3}$")
        return if (regex.matches(input.trim())) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Amount must be in format: 100 EGP")
        }
    }
}