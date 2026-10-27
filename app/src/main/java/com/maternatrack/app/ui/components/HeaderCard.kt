package com.maternatrack.app.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.logic.formatPanjang
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.MtRadius
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul
import java.time.LocalDate

/** Kartu sapaan kuning: tanggal, sapaan, satu kalimat, dan maskot Bunda yang mengambang. */
@Composable
fun HeaderCard(hariIni: LocalDate, lengkap: Boolean, modifier: Modifier = Modifier) {
    val transisi = rememberInfiniteTransition(label = "maskot")
    val ambang by transisi.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(tween(1600), RepeatMode.Reverse),
        label = "ambang"
    )

    Box(
        modifier
            .fillMaxWidth()
            .height(196.dp)
            .clip(RoundedCornerShape(MtRadius.Lg))
            .background(MtColor.Header)
    ) {
        // Lingkaran terang di belakang maskot
        Box(
            Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 70.dp, y = (-10).dp)
                .size(250.dp)
                .clip(CircleShape)
                .background(MtColor.LemonMid)
        )
        Image(
            painter = painterResource(R.drawable.art_maskot),
            contentDescription = "Maskot Bunda",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 26.dp, y = 6.dp)
                .size(200.dp)
                .graphicsLayer { translationY = ambang.dp.toPx() }
        )
        Column(
            Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp)
                .width(180.dp)
        ) {
            Text(formatPanjang(hariIni), style = isi(13, 700), color = MtColor.Ink)
            Spacer(Modifier.height(4.dp))
            Text(
                if (lengkap) "Hebat, Bunda!" else "Halo, Bunda!",
                style = judul(30),
                color = MtColor.Ink
            )
            Spacer(Modifier.height(4.dp))
            Text(
                if (lengkap) "Isi piring hari ini sudah lengkap" else "Yuk lengkapi isi piring hari ini",
                style = isi(14, 600),
                color = MtColor.Ink,
                modifier = Modifier.width(150.dp)
            )
        }
    }
}
