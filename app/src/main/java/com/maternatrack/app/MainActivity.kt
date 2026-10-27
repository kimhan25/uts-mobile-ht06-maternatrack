package com.maternatrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.maternatrack.app.ui.MaternaTrackScreen
import com.maternatrack.app.ui.theme.MaternaTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaternaTrackTheme {
                MaternaTrackScreen()
            }
        }
    }
}
