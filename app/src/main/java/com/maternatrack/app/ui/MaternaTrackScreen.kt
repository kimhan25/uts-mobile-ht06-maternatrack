package com.maternatrack.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.logic.ringkasanHari
import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.model.StatusHarian
import com.maternatrack.app.ui.components.HeaderCard
import com.maternatrack.app.ui.components.PiringHariIniCard
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.judul
import java.time.LocalDate

/**
 * Satu-satunya layar aplikasi. Semua state disimpan di sini (in-memory),
 * lalu diteruskan ke komponen sebagai data + lambda.
 */
@Composable
fun MaternaTrackScreen() {
    val hariIni = remember { LocalDate.now() }

    // Daftar catatan sementara; hilang saat aplikasi ditutup (batasan UTS)
    val catatan = remember { mutableStateListOf<CatatanMakan>() }
    var adaArahanSuplemen by remember { mutableStateOf(true) }

    // Ringkasan dihitung ulang otomatis setiap catatan atau pengaturan suplemen berubah
    val ringkasanHariIni by remember {
        derivedStateOf { ringkasanHari(hariIni, catatan, adaArahanSuplemen) }
    }

    val listState = rememberLazyListState()
    val insets = WindowInsets.systemBars.asPaddingValues()

    Box(Modifier.fillMaxSize().background(MtColor.Bg)) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = insets.calculateTopPadding(),
                bottom = insets.calculateBottomPadding() + 104.dp
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item(key = "appbar") { AppBar() }
            item(key = "header") {
                HeaderCard(hariIni, lengkap = ringkasanHariIni.status == StatusHarian.LENGKAP)
            }
            item(key = "piring") { PiringHariIniCard(ringkasanHariIni, adaArahanSuplemen) }
        }
    }
}

@Composable
private fun AppBar() {
    Row(
        Modifier.padding(top = 6.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painterResource(R.drawable.art_logo), contentDescription = null, modifier = Modifier.size(40.dp))
        Spacer(Modifier.width(10.dp))
        Text("MaternaTrack", style = judul(20), color = MtColor.Ink)
    }
}
