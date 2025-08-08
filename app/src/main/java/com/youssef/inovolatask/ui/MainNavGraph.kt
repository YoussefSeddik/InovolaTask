package com.youssef.inovolatask.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.youssef.inovolatask.ui.addexpense.AddExpenseScreen
import com.youssef.inovolatask.ui.addexpense.AddExpenseViewModel
import com.youssef.inovolatask.ui.dashboard.DashboardScreen
import com.youssef.inovolatask.ui.dashboard.DashboardViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard",
        modifier = modifier
    ) {
        composable("dashboard") {
            val vm: DashboardViewModel = koinViewModel()
            DashboardScreen(vm, navController)
        }
        composable("add") {
            val vm: AddExpenseViewModel = koinViewModel()
            AddExpenseScreen(vm, navController)
        }
    }
}
