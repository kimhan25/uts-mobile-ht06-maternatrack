package com.maternatrack.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.logic.FieldForm
import com.maternatrack.app.logic.FormInput
import com.maternatrack.app.logic.HasilValidasi
import com.maternatrack.app.logic.keCatatan
import com.maternatrack.app.logic.pesanEdukasi
import com.maternatrack.app.logic.ringkasHarian
import com.maternatrack.app.logic.ringkasanHari
import com.maternatrack.app.logic.validasiCatatan
import com.maternatrack.app.logic.waktuBerikutnya
import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.StatusHarian
import com.maternatrack.app.model.WaktuMakan
import com.maternatrack.app.ui.components.Confetti
import com.maternatrack.app.ui.components.DetailHariSheet
import com.maternatrack.app.ui.components.EdukasiCard
import com.maternatrack.app.ui.components.EmptyRiwayat
import com.maternatrack.app.ui.components.FloatingNavBar
import com.maternatrack.app.ui.components.FormCatatan
import com.maternatrack.app.ui.components.HeaderCard
import com.maternatrack.app.ui.components.JudulRiwayat
import com.maternatrack.app.ui.components.PiringHariIniCard
import com.maternatrack.app.ui.components.ProfilSheet
import com.maternatrack.app.ui.components.RiwayatHariCard
import com.maternatrack.app.ui.components.TabNav
import com.maternatrack.app.ui.components.ToastSimpan
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.judul
import java.time.LocalDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val harian by remember { derivedStateOf { ringkasHarian(catatan, adaArahanSuplemen) } }

    // State formulir
    var tanggal by remember { mutableStateOf(hariIni) }
    var waktu by remember { mutableStateOf<WaktuMakan?>(null) }
    // Porsi disimpan sebagai teks; kelompok yang tidak ada di map berarti belum dipilih
    val porsiTeks = remember { mutableStateMapOf<KelompokMakanan, String>() }
    var minumSuplemen by remember { mutableStateOf(false) }
    var cerita by remember { mutableStateOf("") }
    var hasil by remember { mutableStateOf<HasilValidasi?>(null) }
    var pemicuError by remember { mutableIntStateOf(0) }

    // Umpan balik setelah simpan: toast 2 detik, dan confetti jika hari ini menjadi lengkap
    var toast by remember { mutableStateOf<CatatanMakan?>(null) }
    var pemicuConfetti by remember { mutableIntStateOf(0) }
    LaunchedEffect(toast) {
        if (toast != null) {
            delay(2000)
            toast = null
        }
    }

    // Pesan kesalahan sebuah field hilang begitu field itu diubah
    fun hapusError(field: FieldForm? = null, kelompok: KelompokMakanan? = null) {
        val h = hasil ?: return
        hasil = h.copy(
            errorField = h.errorField.filterKeys { it != field },
            errorPorsi = h.errorPorsi.filterKeys { it != kelompok }
        )
    }

    fun kosongkanIsian() {
        porsiTeks.clear()
        minumSuplemen = false
        cerita = ""
        hasil = null
    }

    fun reset() {
        kosongkanIsian()
        tanggal = hariIni
        waktu = null
    }

    fun simpan() {
        val input = FormInput(
            tanggal = tanggal,
            waktu = waktu,
            porsiTeks = porsiTeks.toMap(),
            minumSuplemen = minumSuplemen && adaArahanSuplemen,
            cerita = cerita
        )
        val h = validasiCatatan(input, catatan, LocalDate.now())
        if (!h.valid) {
            hasil = h
            pemicuError++
            return
        }
        val persenSebelum = ringkasanHari(tanggal, catatan, adaArahanSuplemen).persentase
        // Input valid diubah menjadi object lalu ditambahkan ke daftar sementara
        val baru = input.keCatatan(id = System.currentTimeMillis())
        catatan.add(baru)
        val persenSesudah = ringkasanHari(tanggal, catatan, adaArahanSuplemen).persentase
        toast = baru
        if (tanggal == hariIni && persenSebelum < 100 && persenSesudah == 100) pemicuConfetti++
        kosongkanIsian()
        waktu = waktuBerikutnya(tanggal, catatan)
    }

    val tanggalBercatatan by remember { derivedStateOf { catatan.map { it.tanggal }.toSet() } }
    val waktuSudahDicatat by remember {
        derivedStateOf { catatan.filter { it.tanggal == tanggal }.map { it.waktu }.toSet() }
    }

    // Tanggal yang sedang dibuka di sheet Detail Hari (null = sheet tertutup)
    var detailTanggal by remember { mutableStateOf<LocalDate?>(null) }
    var profilTerbuka by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Urutan item LazyColumn: appbar(0), header(1), piring(2), form(3), riwayat_judul(4),
    // kartu hari / empty state, lalu edukasi
    val indeksForm = 3
    val indeksRiwayat = 4
    val indeksEdukasi = indeksRiwayat + 1 + maxOf(harian.size, 1)

    // Tab aktif mengikuti bagian yang sedang terlihat saat Bunda menggulir
    val tabAktif by remember {
        derivedStateOf {
            val pertama = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.key as? String ?: "appbar"
            when {
                !listState.canScrollForward && listState.canScrollBackward -> TabNav.EDUKASI
                pertama in listOf("appbar", "header", "piring", "form") -> TabNav.BERANDA
                pertama == "edukasi" -> TabNav.EDUKASI
                else -> TabNav.RIWAYAT
            }
        }
    }

    // Tab yang baru diketuk tetap disorot sampai Bunda menggulir sendiri
    // (penting saat isi layar pendek dan bagian tujuan tidak bisa naik ke atas)
    var tabDiketuk by remember { mutableStateOf<TabNav?>(null) }
    val sedangDiseret by listState.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(sedangDiseret) { if (sedangDiseret) tabDiketuk = null }

    fun gulirKe(indeks: Int) {
        scope.launch { listState.animateScrollToItem(indeks) }
    }
    val bawah = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    // statusBarsPadding: daftar dimulai di bawah status bar sehingga konten tidak tertutup jam/ikon
    Box(Modifier.fillMaxSize().background(MtColor.Bg).statusBarsPadding()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = bawah + 104.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item(key = "appbar") { AppBar() }
            item(key = "header") {
                HeaderCard(
                    hariIni,
                    lengkap = ringkasanHariIni.status == StatusHarian.LENGKAP,
                    pemicuLompat = pemicuConfetti
                )
            }
            item(key = "piring") { PiringHariIniCard(ringkasanHariIni, adaArahanSuplemen) }
            item(key = "form") {
                FormCatatan(
                    hariIni = hariIni,
                    tanggal = tanggal,
                    onTanggal = { baru ->
                        tanggal = baru
                        hapusError(FieldForm.TANGGAL)
                        // Waktu yang sudah dicatat di tanggal baru tidak boleh tetap terpilih
                        val w = waktu
                        if (w != null && !w.bolehBerulang && catatan.any { it.tanggal == baru && it.waktu == w }) {
                            waktu = null
                        }
                    },
                    tanggalBercatatan = tanggalBercatatan,
                    waktu = waktu,
                    onWaktu = {
                        waktu = it
                        hapusError(FieldForm.WAKTU)
                    },
                    waktuSudahDicatat = waktuSudahDicatat,
                    porsiTeks = porsiTeks,
                    onPorsi = { kelompok, teks ->
                        if (teks == null) porsiTeks.remove(kelompok) else porsiTeks[kelompok] = teks
                        hapusError(FieldForm.ISI_PIRING, kelompok)
                    },
                    minumSuplemen = minumSuplemen,
                    onSuplemen = {
                        minumSuplemen = it
                        hapusError(FieldForm.ISI_PIRING)
                    },
                    adaArahanSuplemen = adaArahanSuplemen,
                    onBukaProfil = { profilTerbuka = true },
                    cerita = cerita,
                    onCerita = { cerita = it },
                    onSimpan = ::simpan,
                    onReset = ::reset,
                    hasil = hasil,
                    pemicuError = pemicuError
                )
            }

            item(key = "riwayat_judul") { JudulRiwayat(catatan.size) }
            if (harian.isEmpty()) {
                item(key = "riwayat_kosong") { EmptyRiwayat() }
            } else {
                // Satu kartu per tanggal; animateItem membuat kartu baru meluncur masuk
                items(harian, key = { "hari_${it.tanggal}" }) { hari ->
                    RiwayatHariCard(
                        ringkasan = hari,
                        hariIni = hariIni,
                        onDetail = { detailTanggal = hari.tanggal },
                        modifier = Modifier.animateItem()
                    )
                }
            }

            item(key = "edukasi") { EdukasiCard(pesanEdukasi(ringkasanHariIni)) }
        }

        // Toast muncul di atas navbar; catatan terakhir diingat agar animasi keluar tetap berisi
        var toastTerakhir by remember { mutableStateOf<CatatanMakan?>(null) }
        if (toast != null) toastTerakhir = toast
        AnimatedVisibility(
            visible = toast != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 88.dp)
        ) {
            toastTerakhir?.let { ToastSimpan(it) }
        }

        FloatingNavBar(
            aktif = tabDiketuk ?: tabAktif,
            onTab = { tab ->
                tabDiketuk = tab
                when (tab) {
                    TabNav.BERANDA -> gulirKe(0)
                    TabNav.RIWAYAT -> gulirKe(indeksRiwayat)
                    TabNav.EDUKASI -> gulirKe(indeksEdukasi)
                }
            },
            onCatat = {
                // Tombol + : ke form dan langsung pilih waktu makan berikutnya yang belum dicatat
                if (waktu == null) waktu = waktuBerikutnya(tanggal, catatan)
                tabDiketuk = TabNav.BERANDA
                gulirKe(indeksForm)
            },
            onProfil = { profilTerbuka = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        )

        Confetti(pemicuConfetti)
    }

    val detail = harian.firstOrNull { it.tanggal == detailTanggal }
    if (detail != null) {
        DetailHariSheet(detail, adaArahanSuplemen, onTutup = { detailTanggal = null })
    }
    if (profilTerbuka) {
        ProfilSheet(
            adaArahanSuplemen = adaArahanSuplemen,
            onUbahArahan = { ada ->
                adaArahanSuplemen = ada
                // Tanpa arahan nakes, centang suplemen di form dibatalkan
                if (!ada) minumSuplemen = false
            },
            onTutup = { profilTerbuka = false }
        )
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
