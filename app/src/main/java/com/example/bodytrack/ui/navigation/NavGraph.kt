package com.example.bodytrack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bodytrack.ui.screens.AddEntryScreen
import com.example.bodytrack.ui.screens.CompareScreen
import com.example.bodytrack.ui.screens.HomeScreen
import com.example.bodytrack.ui.screens.StatsScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable(route = "home") {
            HomeScreen(navController)
        }

        composable(route = "add_entry") {
            AddEntryScreen(navController)   // ← no navController here
        }

        composable(route = "compare") {
            CompareScreen(navController)
        }

        composable(route = "stats") {
            StatsScreen()
        }
    }


}
