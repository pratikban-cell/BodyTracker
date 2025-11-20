package com.example.bodytrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.bodytrack.ui.navigation.AppNavGraph
import com.example.bodytrack.ui.theme.BodyTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BodyTrackTheme {
                AppNavGraph()   // uses your NavGraph with Home/Add/Edit/Compare/Stats
            }
        }
    }
}
