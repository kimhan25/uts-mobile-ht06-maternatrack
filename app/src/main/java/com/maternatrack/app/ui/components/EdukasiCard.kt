package com.maternatrack.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.logic.DISCLAIMER
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.MtRadius
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul

/** Bagian Edukasi Gizi: satu pesan umum yang menyesuaikan isi piring hari ini. */
@Composable
fun EdukasiCard(pesan: String) {
    Column {
        Text("Edukasi Gizi", style = judul(20), color = MtColor.Ink)
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .background(MtColor.Surface, RoundedCornerShape(MtRadius.Lg))
                .padding(start = 10.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(R.drawable.art_lampu), null, Modifier.size(72.dp))
            Spacer(Modifier.width(8.dp))
            Column {
                Text("Tahukah Bunda?", style = judul(16), color = MtColor.Ink)
                Spacer(Modifier.height(4.dp))
                AnimatedContent(pesan, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "pesan") {
                    Text(it, style = isi(13, 600), color = MtColor.Ink)
                }
                Spacer(Modifier.height(4.dp))
                Text(DISCLAIMER, style = isi(11, 700), color = MtColor.InkMuted)
            }
        }
    }
}
