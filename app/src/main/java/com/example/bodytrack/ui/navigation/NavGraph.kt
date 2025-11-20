// NavGraph.kt
package com.example.bodytrack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bodytrack.ui.screens.AddEntryScreen
import com.example.bodytrack.ui.screens.CompareScreen
import com.example.bodytrack.ui.screens.EditEntryScreen
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
            AddEntryScreen(navController)
        }

        composable(route = "stats") {
            StatsScreen()
        }

        composable(
            route = "edit_entry/{entryId}",
            arguments = listOf(
                navArgument("entryId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("entryId") ?: return@composable
            EditEntryScreen(navController, id)
        }

        composable(
            route = "compare/{entryId}",
            arguments = listOf(
                navArgument("entryId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("entryId") ?: return@composable
            CompareScreen(navController, id)
        }
    }
}
