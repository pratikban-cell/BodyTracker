// CompareScreen.kt
package com.example.bodytrack.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
    entryId: Int,
    viewModel: EntryViewModel = viewModel()
) {
    val allEntries by viewModel.allEntries.collectAsState()

    // Left: entry tapped on HomeScreen
    val leftEntry = allEntries.find { it.id == entryId }

    // Right: user chooses another entry
    var rightEntry by remember { mutableStateOf<EntryEntity?>(null) }
    var pickerOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compare Progress") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (leftEntry == null) {
                Text("Entry not found.")
                return@Column
            }

            // ------------------------
            // PICK SECOND ENTRY
            // ------------------------
            Text("Tap to pick an entry to compare with:")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { pickerOpen = true }
                    .padding(12.dp)
            ) {
                Text(
                    text = rightEntry?.let { formatDate(it.date) }
                        ?: "Choose entry"
                )
            }

            if (pickerOpen) {
                AlertDialog(
                    onDismissRequest = { pickerOpen = false },
                    confirmButton = {},
                    title = { Text("Choose comparison entry") },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            allEntries
                                .filter { it.id != leftEntry.id }
                                .forEach { e ->
                                    Text(
                                        text = "${formatDate(e.date)}  (${e.weight} kg)",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                rightEntry = e
                                                pickerOpen = false
                                            }
                                            .padding(vertical = 4.dp)
                                    )
                                }
                        }
                    }
                )
            }

            // ------------------------
            // SIDE-BY-SIDE IMAGES
            // ------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ComparePhotoColumn(
                    label = "Left",
                    entry = leftEntry,
                    modifier = Modifier.weight(1f)
                )
                ComparePhotoColumn(
                    label = "Right",
                    entry = rightEntry,
                    modifier = Modifier.weight(1f)
                )
            }

            // ------------------------
            // WEIGHT INFO
            // ------------------------
            Text(
                text = if (rightEntry != null) {
                    val diff = rightEntry!!.weight - leftEntry.weight
                    "Weight: ${leftEntry.weight} kg → ${rightEntry!!.weight} kg  (Δ ${"%.1f".format(diff)} kg)"
                } else {
                    "Pick a second entry to see weight difference."
                }
            )
        }
    }
}

@Composable
private fun ComparePhotoColumn(
    label: String,
    entry: EntryEntity?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(label)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center
        ) {
            if (entry == null) {
                Text("No photo")
            } else {
                val bitmap = BitmapFactory.decodeFile(entry.imagePath)
                val img = bitmap?.asImageBitmap()
                if (img != null) {
                    Image(
                        bitmap = img,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Image error")
                }
            }
        }

        if (entry != null) {
            Text("${entry.weight} kg")
            Text(
                formatDate(entry.date),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
