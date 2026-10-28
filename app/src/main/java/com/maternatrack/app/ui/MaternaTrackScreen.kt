package com.maternatrack.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
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
import com.maternatrack.app.ui.components.DetailHariSheet
import com.maternatrack.app.ui.components.EdukasiCard
import com.maternatrack.app.ui.components.EmptyRiwayat
import com.maternatrack.app.ui.components.FloatingNavBar
import com.maternatrack.app.ui.components.FormCatatan
import com.maternatrack.app.ui.components.HeaderCard
import com.maternatrack.app.ui.components.InfoSimpan
import com.maternatrack.app.ui.components.JudulRiwayat
import com.maternatrack.app.ui.components.PiringHariIniCard
import com.maternatrack.app.ui.components.PopupHariLengkap
import com.maternatrack.app.ui.components.ProfilSheet
import com.maternatrack.app.ui.components.RiwayatHariCard
import com.maternatrack.app.ui.components.TinggiNavBar
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
@OptIn(ExperimentalLayoutApi::class)
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

    // Umpan balik setelah simpan: toast 2,5 detik, atau pop-up perayaan jika hari ini menjadi lengkap
    var toast by remember { mutableStateOf<InfoSimpan?>(null) }
    var pemicuLengkap by remember { mutableIntStateOf(0) }
    var popupLengkap by remember { mutableStateOf(false) }
    LaunchedEffect(toast) {
        if (toast != null) {
            delay(2500)
            toast = null
        }
    }

    // Getaran halus: berhasil saat simpan, menolak saat ada isian yang salah
    val haptik = LocalHapticFeedback.current

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
            haptik.performHapticFeedback(HapticFeedbackType.Reject)
            return
        }
        val persenSebelum = ringkasanHari(tanggal, catatan, adaArahanSuplemen).persentase
        // Input valid diubah menjadi object lalu ditambahkan ke daftar sementara
        val baru = input.keCatatan(id = System.currentTimeMillis())
        catatan.add(baru)
        val persenSesudah = ringkasanHari(tanggal, catatan, adaArahanSuplemen).persentase
        haptik.performHapticFeedback(HapticFeedbackType.Confirm)
        if (tanggal == hariIni && persenSebelum < 100 && persenSesudah == 100) {
            // Pop-up menggantikan toast agar perayaan tidak bertumpuk dengan notifikasi biasa
            pemicuLengkap++
            popupLengkap = true
        } else {
            toast = InfoSimpan(baru, persenSesudah)
        }
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

    // Navbar sembunyi saat Bunda menggulir ke bawah dan muncul lagi saat menggulir ke atas.
    // NestedScrollConnection hanya menerima gulir dari jari, jadi gulir otomatis tidak ikut.
    var navDiminta by remember { mutableStateOf(true) }
    val ambangGulir = with(LocalDensity.current) { 16.dp.toPx() }
    val pantauGulir = remember {
        object : NestedScrollConnection {
            var akumulasi = 0f
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val dy = available.y
                // Mulai hitung ulang setiap arah gulir berubah
                if (dy != 0f && (dy > 0f) != (akumulasi > 0f)) akumulasi = 0f
                akumulasi += dy
                if (akumulasi < -ambangGulir) navDiminta = false
                if (akumulasi > ambangGulir) navDiminta = true
                return Offset.Zero
            }
        }
    }
    val keyboardTerbuka = WindowInsets.isImeVisible
    val navTerlihat by remember {
        derivedStateOf {
            // Di puncak layar navbar selalu tampil; selain itu hanya muncul lagi saat menggulir ke atas
            navDiminta || !listState.canScrollBackward
        }
    }
    val geserNav = animateFloatAsState(
        if (navTerlihat && !keyboardTerbuka) 0f else 1f,
        spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessMediumLow),
        label = "geserNav"
    )

    fun gulirKeAtas() {
        navDiminta = true
        scope.launch { listState.animateScrollToItem(0) }
    }
    val bawah = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    // Box luar menampung pop-up agar lapisan gelapnya ikut menutupi status bar
    Box(Modifier.fillMaxSize()) {
        // statusBarsPadding: daftar dimulai di bawah status bar sehingga konten tidak tertutup jam/ikon
        Box(
            Modifier
                .fillMaxSize()
                .background(MtColor.Bg)
                .statusBarsPadding()
                // Daftar menyusut saat keyboard muncul sehingga kolom yang diketik tetap terlihat
                .imePadding()
                .nestedScroll(pantauGulir)
        ) {
            LazyColumn(
                state = listState,
                // Di layar lebar (landscape/tablet) isi tetap selebar ponsel dan berada di tengah
                modifier = Modifier.align(Alignment.TopCenter).widthIn(max = 560.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = bawah + TinggiNavBar + 36.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item(key = "appbar") { AppBar() }
                item(key = "header") {
                    HeaderCard(
                        hariIni,
                        lengkap = ringkasanHariIni.status == StatusHarian.LENGKAP,
                        pemicuLompat = pemicuLengkap
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

            FloatingNavBar(
                onBeranda = ::gulirKeAtas,
                onProfil = { profilTerbuka = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 12.dp)
                    .graphicsLayer {
                        // Turun sejauh tinggi navbar + jarak bawah, nilai animasi dibaca di fase gambar saja
                        translationY = geserNav.value * (size.height + 12.dp.toPx() + bawah.toPx())
                    }
            )

            // Toast turun dari atas; data terakhir diingat agar animasi keluar tetap berisi
            var toastTerakhir by remember { mutableStateOf<InfoSimpan?>(null) }
            if (toast != null) toastTerakhir = toast
            AnimatedVisibility(
                visible = toast != null,
                enter = slideInVertically(spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow)) { -it } +
                    fadeIn(),
                exit = slideOutVertically { -it } + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp)
            ) {
                toastTerakhir?.let { ToastSimpan(it, hariIni, onTutup = { toast = null }) }
            }

        }

        AnimatedVisibility(visible = popupLengkap, enter = fadeIn(), exit = fadeOut()) {
            PopupHariLengkap(adaArahanSuplemen, onTutup = { popupLengkap = false })
        }
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
        Image(painterResource(R.drawable.art_logo), contentDescription = null, modifier = Modifier.size(32.dp))
        Spacer(Modifier.width(10.dp))
        Text("MaternaTrack", style = judul(20), color = MtColor.Ink)
    }
}
