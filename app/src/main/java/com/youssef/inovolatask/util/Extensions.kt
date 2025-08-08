package com.youssef.inovolatask.util

import java.util.Locale

fun Double.format(): String = String.format(Locale.US, "%.2f", this)
