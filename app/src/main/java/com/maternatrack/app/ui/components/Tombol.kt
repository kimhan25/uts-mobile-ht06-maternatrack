package com.maternatrack.app.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.MtRadius
import com.maternatrack.app.ui.theme.judul

/** Tombol primer: latar pink, teks putih (Simpan, Tutup, Mengerti). */
@Composable
fun TombolUtama(teks: String, onKlik: () -> Unit, modifier: Modifier = Modifier, ikon: ImageVector? = null) {
    val sumber = remember { MutableInteractionSource() }
    Row(
        modifier
            .fillMaxWidth()
            .height(52.dp)
            .mengecilSaatDitekan(sumber)
            .clip(RoundedCornerShape(MtRadius.Md))
            .background(MtColor.Primary)
            .clickable(interactionSource = sumber, indication = ripple(), onClick = onKlik),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (ikon != null) {
            Icon(ikon, null, tint = MtColor.Surface, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(teks, style = judul(17), color = MtColor.Surface)
    }
}

/** Tombol sekunder: latar putih bergaris (Reset). */
@Composable
fun TombolGaris(teks: String, ikon: ImageVector, onKlik: () -> Unit, modifier: Modifier = Modifier) {
    val sumber = remember { MutableInteractionSource() }
    Row(
        modifier
            .height(52.dp)
            .mengecilSaatDitekan(sumber)
            .clip(RoundedCornerShape(MtRadius.Md))
            .background(MtColor.Surface)
            .border(1.5.dp, MtColor.Line, RoundedCornerShape(MtRadius.Md))
            .clickable(interactionSource = sumber, indication = ripple(), onClick = onKlik)
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(ikon, null, tint = MtColor.Ink, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(6.dp))
        Text(teks, style = judul(16), color = MtColor.Ink)
    }
}

/** Pegangan kecil di atas lembar bawah. */
@Composable
fun PegangSheet() {
    Box(
        Modifier
            .padding(vertical = 12.dp)
            .size(width = 40.dp, height = 5.dp)
            .background(MtColor.Line, CircleShape)
    )
}

/** Tombol mengecil sedikit saat ditekan; skala dibaca di graphicsLayer agar tidak recompose per frame. */
@Composable
private fun Modifier.mengecilSaatDitekan(sumber: MutableInteractionSource): Modifier {
    val ditekan by sumber.collectIsPressedAsState()
    val skala = animateFloatAsState(
        if (ditekan) 0.96f else 1f,
        spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "skalaTekan"
    )
    return graphicsLayer {
        scaleX = skala.value
        scaleY = skala.value
    }
}
