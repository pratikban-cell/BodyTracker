package com.example.bodytrack.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bodytrack.data.EntryEntity
import com.example.bodytrack.viewmodel.EntryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    navController: NavController,
    viewModel: EntryViewModel = viewModel()
) {
    var entries by remember { mutableStateOf<List<EntryEntity>>(emptyList()) }

    var selected1 by remember { mutableStateOf<EntryEntity?>(null) }
    var selected2 by remember { mutableStateOf<EntryEntity?>(null) }

    // Load entries from database
    LaunchedEffect(true) {
        entries = viewModel.getAllEntries()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compare Photos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // --- Display Comparison ---
            if (selected1 != null || selected2 != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    ComparePhotoBox(selected1)
                    ComparePhotoBox(selected2)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "Tap any two photos to compare",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- List of entries user can tap ---
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(entries) { entry ->
                    CompareListItem(
                        entry = entry,
                        onSelect = { chosen ->
                            if (selected1 == null) selected1 = chosen
                            else selected2 = chosen
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CompareListItem(entry: EntryEntity, onSelect: (EntryEntity) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(entry) },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val bitmap = BitmapFactory.decodeFile(entry.imagePath)
            val imageBitmap = bitmap?.asImageBitmap()

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text("Weight: ${entry.weight}")
                Text("Date: ${formatDate(entry.date)}")
            }
        }
    }
}

@Composable
fun ComparePhotoBox(entry: EntryEntity?) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {

        if (entry == null) {
            Text("Select\nPhoto", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        } else {

            val bitmap = BitmapFactory.decodeFile(entry.imagePath)
            val imageBitmap = bitmap?.asImageBitmap()

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    modifier = Modifier.size(150.dp)
                )
            }
        }
    }
}
