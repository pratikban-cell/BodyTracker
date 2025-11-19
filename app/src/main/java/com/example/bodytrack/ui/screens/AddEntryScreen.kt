package com.example.bodytrack.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bodytrack.viewmodel.EntryViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import android.widget.Toast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(navController: NavController) {

    val context = LocalContext.current
    val entryViewModel: EntryViewModel = viewModel()

    var weight by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var tempPhotoFile by remember { mutableStateOf<File?>(null) }

    // ----------------------------------- GALLERY PICKER -----------------------------------
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream: InputStream? =
                context.contentResolver.openInputStream(uri)

            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Save gallery file as a real file for DB
            val imagesDir = File(context.filesDir, "images")
            if (!imagesDir.exists()) imagesDir.mkdirs()

            val file = File(imagesDir, "gallery_${System.currentTimeMillis()}.jpg")
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()

            tempPhotoFile = file
            selectedImage = bitmap.asImageBitmap()
        }
    }

    // ----------------------------------- CAMERA PICKER -----------------------------------
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoFile != null) {
            val bitmap = BitmapFactory.decodeFile(tempPhotoFile!!.absolutePath)
            selectedImage = bitmap.asImageBitmap()
        }
    }

    // ----------------------------------- UI LAYOUT -----------------------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Entry") },
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ----------------------------------- IMAGE PREVIEW -----------------------------------
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImage != null) {
                    Image(
                        bitmap = selectedImage!!,
                        contentDescription = "Selected Photo",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("No photo selected")
                }
            }

            // Pick from gallery
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { galleryLauncher.launch("image/*") }
            ) {
                Text("Choose From Gallery")
            }

            // Take photo
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val imagesDir = File(context.filesDir, "images")
                    if (!imagesDir.exists()) imagesDir.mkdirs()

                    val temp = File(imagesDir, "photo_${System.currentTimeMillis()}.jpg")
                    tempPhotoFile = temp

                    val uri = FileProvider.getUriForFile(
                        context,
                        context.packageName + ".provider",
                        temp
                    )
                    cameraLauncher.launch(uri)
                }
            ) {
                Text("Take Photo")
            }

            // ----------------------------------- WEIGHT INPUT -----------------------------------
            OutlinedTextField(
                value = weight,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        weight = newValue
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Enter Weight") },
                modifier = Modifier.fillMaxWidth()
            )

            // ----------------------------------- SAVE ENTRY -----------------------------------
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (selectedImage != null && weight.isNotEmpty() && tempPhotoFile != null) {

                        entryViewModel.saveEntry(
                            weight = weight.toDouble(),
                            date = System.currentTimeMillis(),
                            imagePath = tempPhotoFile!!.absolutePath
                        )

                        Toast.makeText(context, "Entry Saved!", Toast.LENGTH_SHORT).show()

                        navController.popBackStack() // Go back to HomeScreen
                    }
                }
            ) {
                Text("Save Entry")
            }
        }
    }
}
