package com.maternatrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.maternatrack.app.ui.theme.MaternaTrackTheme
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.judul

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaternaTrackTheme {
                Box(
                    modifier = Modifier.fillMaxSize().background(MtColor.Bg),
                    contentAlignment = Alignment.Center
                ) {
                    Text("MaternaTrack", style = judul(24), color = MtColor.Ink)
                }
            }
        }
    }
}
