package com.example.bodytrack.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bodytrack.viewmodel.EntryViewModel
import com.example.bodytrack.data.EntryEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: EntryViewModel = viewModel()
) {
    // Automatically updates when DB changes
    val entries by viewModel.allEntries.collectAsState()

    var confirmDelete by remember { mutableStateOf<EntryEntity?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("BodyTracker") }) },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add_entry")
            }) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Your Progress",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                items(entries) { entry ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* Click card if needed */ },
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                val bitmap = BitmapFactory.decodeFile(entry.imagePath)
                                val imageBitmap = bitmap?.asImageBitmap()

                                if (imageBitmap != null) {
                                    Image(
                                        bitmap = imageBitmap,
                                        contentDescription = "Photo",
                                        modifier = Modifier.size(90.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text("Weight: ${entry.weight}")
                                    Text("Date: ${formatDate(entry.date)}")
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                // EDIT entry
                                IconButton(onClick = {
                                    navController.navigate("edit_entry/${entry.id}")
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Entry")
                                }

                                // COMPARE entry (this entry vs choose second one)
                                IconButton(onClick = {
                                    navController.navigate("compare/${entry.id}")
                                }) {
                                    Text("Compare")
                                }




                                // DELETE entry
                                IconButton(onClick = {
                                    confirmDelete = entry
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Entry")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ==========================
    // DELETE CONFIRMATION DIALOG
    // ==========================
    if (confirmDelete != null) {
        AlertDialog(
            onDismissRequest = { confirmDelete = null },

            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteEntry(confirmDelete!!)
                    confirmDelete = null
                }) { Text("Delete") }
            },

            dismissButton = {
                TextButton(onClick = { confirmDelete = null }) {
                    Text("Cancel")
                }
            },

            title = { Text("Delete Entry") },
            text = { Text("Are you sure you want to delete this entry?") }
        )
    }
}

// -----------------------
// DATE FORMATTER
// -----------------------
fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}
