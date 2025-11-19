package com.example.bodytrack.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun StatsScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Stats") }) }
    ) { padding ->
        Box(
            modifier = androidx.compose.ui.Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text("Stats Screen")
        }
    }
}
