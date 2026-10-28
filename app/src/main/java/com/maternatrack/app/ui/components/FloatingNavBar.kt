package com.maternatrack.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.ui.theme.MtColor

private val BentukBar = RoundedCornerShape(24.dp)
private val BentukTab = RoundedCornerShape(18.dp)

/** Tinggi navbar: tab 52dp + padding 10dp atas dan bawah. */
val TinggiNavBar = 72.dp

/**
 * Navbar pendek di tengah bawah layar. Di UTS hanya Beranda dan Profil yang aktif;
 * Riwayat dan Edukasi tampil redup karena layarnya baru dibuat di UAS.
 */
@Composable
fun FloatingNavBar(onBeranda: () -> Unit, onProfil: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(BentukBar)
            .background(MtColor.Primary)
            // Ketukan di area navbar (termasuk tab yang belum aktif) tidak boleh tembus ke kartu di belakangnya
            .pointerInput(Unit) { detectTapGestures { } }
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemTab(Icons.Rounded.Home, "Beranda", aktif = true, onKlik = onBeranda)
        ItemTab(Icons.Rounded.History, "Riwayat (tersedia di UAS)", aktif = false, onKlik = null)
        ItemTab(Icons.AutoMirrored.Rounded.MenuBook, "Edukasi (tersedia di UAS)", aktif = false, onKlik = null)
        Box(
            Modifier.size(width = 60.dp, height = 52.dp).clip(BentukTab).clickable(onClick = onProfil),
            contentAlignment = Alignment.Center
        ) {
            // Logo langsung di atas bar pink, dibingkai garis putih tipis agar terpisah dari latar
            Image(
                painterResource(R.drawable.art_logo),
                "Profil Bunda",
                Modifier
                    .size(38.dp)
                    .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape)
            )
        }
    }
}

/** [onKlik] null berarti tab belum tersedia: redup dan tidak bisa diketuk. */
@Composable
private fun ItemTab(ikon: ImageVector, label: String, aktif: Boolean, onKlik: (() -> Unit)?) {
    val dasar = Modifier
        .size(width = 64.dp, height = 52.dp)
        .clip(BentukTab)
        .background(if (aktif) MtColor.Surface else Color.Transparent)
    val modifier = if (onKlik != null) {
        dasar.clickable(onClick = onKlik).semantics {
            role = Role.Tab
            selected = aktif
            contentDescription = label
        }
    } else {
        dasar.alpha(0.45f).semantics {
            disabled()
            contentDescription = label
        }
    }
    Box(modifier, contentAlignment = Alignment.Center) {
        Icon(ikon, null, tint = if (aktif) MtColor.Primary else MtColor.Surface, modifier = Modifier.size(26.dp))
    }
}
