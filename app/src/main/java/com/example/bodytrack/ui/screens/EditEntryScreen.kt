package com.example.bodytrack.ui.screens

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bodytrack.viewmodel.EntryViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntryScreen(
    navController: NavController,
    entryId: Int,
    viewModel: EntryViewModel = viewModel()
) {
    val entries by viewModel.allEntries.collectAsState()
    val entry = entries.firstOrNull { it.id == entryId }

    if (entry == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Entry not found.")
        }
        return
    }

    var weight by remember { mutableStateOf(entry.weight.toString()) }
    var imageBitmap = remember(entry.imagePath) {
        BitmapFactory.decodeFile(entry.imagePath)?.asImageBitmap()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Entry") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Image preview
            Box(
                Modifier
                    .size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                imageBitmap?.let {
                    Image(bitmap = it, contentDescription = null)
                } ?: Text("No Image")
            }

            // Weight field
            OutlinedTextField(
                value = weight,
                onValueChange = { if (it.all(Char::isDigit)) weight = it },
                label = { Text("Weight") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Save
            Button(
                onClick = {
                    viewModel.updateEntry(
                        entry.copy(weight = weight.toDouble())
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            // Delete
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                onClick = {
                    viewModel.deleteEntry(entry)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Entry")
            }
        }
    }
}
