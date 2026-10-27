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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.logic.HasilValidasi
import com.maternatrack.app.logic.ringkasanHari
import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.StatusHarian
import com.maternatrack.app.model.WaktuMakan
import com.maternatrack.app.ui.components.FormCatatan
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

    // State formulir
    var tanggal by remember { mutableStateOf(hariIni) }
    var waktu by remember { mutableStateOf<WaktuMakan?>(null) }
    // Porsi disimpan sebagai teks; kelompok yang tidak ada di map berarti belum dipilih
    val porsiTeks = remember { mutableStateMapOf<KelompokMakanan, String>() }
    var minumSuplemen by remember { mutableStateOf(false) }
    var hasil by remember { mutableStateOf<HasilValidasi?>(null) }
    var pemicuError by remember { mutableIntStateOf(0) }

    val tanggalBercatatan by remember { derivedStateOf { catatan.map { it.tanggal }.toSet() } }
    val waktuSudahDicatat by remember {
        derivedStateOf { catatan.filter { it.tanggal == tanggal }.map { it.waktu }.toSet() }
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
            item(key = "form") {
                FormCatatan(
                    hariIni = hariIni,
                    tanggal = tanggal,
                    onTanggal = { baru ->
                        tanggal = baru
                        // Waktu yang sudah dicatat di tanggal baru tidak boleh tetap terpilih
                        val w = waktu
                        if (w != null && !w.bolehBerulang && catatan.any { it.tanggal == baru && it.waktu == w }) {
                            waktu = null
                        }
                    },
                    tanggalBercatatan = tanggalBercatatan,
                    waktu = waktu,
                    onWaktu = { waktu = it },
                    waktuSudahDicatat = waktuSudahDicatat,
                    porsiTeks = porsiTeks,
                    onPorsi = { kelompok, teks ->
                        if (teks == null) porsiTeks.remove(kelompok) else porsiTeks[kelompok] = teks
                    },
                    minumSuplemen = minumSuplemen,
                    onSuplemen = { minumSuplemen = it },
                    adaArahanSuplemen = adaArahanSuplemen,
                    hasil = hasil,
                    pemicuError = pemicuError
                )
            }
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
