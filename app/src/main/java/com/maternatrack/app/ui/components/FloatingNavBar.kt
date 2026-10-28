package com.maternatrack.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.ui.theme.MtColor

/** Bagian layar yang dituju tab navbar (di UTS semuanya ada di satu layar). */
enum class TabNav { BERANDA, RIWAYAT, EDUKASI }

/** Navbar mengambang berbentuk kapsul: hanya ikon, tab aktif berlatar pink lembut. */
@Composable
fun FloatingNavBar(
    aktif: TabNav,
    onTab: (TabNav) -> Unit,
    onCatat: () -> Unit,
    onProfil: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(CircleShape)
            .background(MtColor.Surface)
            .border(1.5.dp, MtColor.Line, CircleShape)
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemTab(Icons.Rounded.Home, "Beranda", aktif == TabNav.BERANDA) { onTab(TabNav.BERANDA) }
        ItemTab(Icons.Rounded.History, "Riwayat", aktif == TabNav.RIWAYAT) { onTab(TabNav.RIWAYAT) }
        Box(
            Modifier.size(52.dp).clip(CircleShape).background(MtColor.Primary).clickable(onClick = onCatat),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Add, "Catat makan", tint = MtColor.Surface, modifier = Modifier.size(28.dp))
        }
        ItemTab(Icons.AutoMirrored.Rounded.MenuBook, "Edukasi", aktif == TabNav.EDUKASI) { onTab(TabNav.EDUKASI) }
        Box(
            Modifier.size(width = 64.dp, height = 52.dp).clip(CircleShape).clickable(onClick = onProfil),
            contentAlignment = Alignment.Center
        ) {
            Box(Modifier.size(36.dp).background(MtColor.Header, CircleShape), contentAlignment = Alignment.Center) {
                Image(painterResource(R.drawable.art_logo), "Profil Bunda", Modifier.size(26.dp))
            }
        }
    }
}

@Composable
private fun ItemTab(ikon: ImageVector, label: String, aktif: Boolean, onKlik: () -> Unit) {
    val latar by animateColorAsState(if (aktif) MtColor.PinkSoft else MtColor.Surface, label = "latarTab")
    val warna by animateColorAsState(if (aktif) MtColor.Primary else MtColor.InkMuted, label = "warnaTab")
    Box(
        Modifier.size(width = 64.dp, height = 52.dp).clip(CircleShape).background(latar).clickable(onClick = onKlik),
        contentAlignment = Alignment.Center
    ) {
        Icon(ikon, label, tint = warna, modifier = Modifier.size(24.dp))
    }
}
