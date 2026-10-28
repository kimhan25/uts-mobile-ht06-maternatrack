package com.maternatrack.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.maternatrack.app.logic.CERITA_MAKS
import com.maternatrack.app.logic.FieldForm
import com.maternatrack.app.logic.HasilValidasi
import com.maternatrack.app.logic.formatPendek
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.WaktuMakan
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.MtRadius
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul
import java.time.LocalDate

/** Formulir "Catat Makan". Semua nilai berasal dari state di layar utama. */
@Composable
fun FormCatatan(
    hariIni: LocalDate,
    tanggal: LocalDate,
    onTanggal: (LocalDate) -> Unit,
    tanggalBercatatan: Set<LocalDate>,
    waktu: WaktuMakan?,
    onWaktu: (WaktuMakan) -> Unit,
    waktuSudahDicatat: Set<WaktuMakan>,
    porsiTeks: Map<KelompokMakanan, String>,
    onPorsi: (KelompokMakanan, String?) -> Unit,
    minumSuplemen: Boolean,
    onSuplemen: (Boolean) -> Unit,
    adaArahanSuplemen: Boolean,
    onBukaProfil: () -> Unit,
    cerita: String,
    onCerita: (String) -> Unit,
    onSimpan: () -> Unit,
    onReset: () -> Unit,
    hasil: HasilValidasi?,
    pemicuError: Int
) {
    // Setiap Simpan gagal, layar digulir ke bagian pertama yang salah
    val keTanggal = remember { BringIntoViewRequester() }
    val keWaktu = remember { BringIntoViewRequester() }
    val keIsiPiring = remember { BringIntoViewRequester() }
    LaunchedEffect(pemicuError) {
        val h = hasil ?: return@LaunchedEffect
        when {
            FieldForm.TANGGAL in h.errorField -> keTanggal.bringIntoView()
            FieldForm.WAKTU in h.errorField -> keWaktu.bringIntoView()
            FieldForm.ISI_PIRING in h.errorField || h.errorPorsi.isNotEmpty() -> keIsiPiring.bringIntoView()
        }
    }

    Column {
        Text("Catat Makan", style = judul(20), color = MtColor.Ink)
        Spacer(Modifier.height(2.dp))
        Text("Pilih waktu makan, lalu isi piringnya", style = isi(13, 600), color = MtColor.InkMuted)
        Spacer(Modifier.height(12.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .background(MtColor.Surface, RoundedCornerShape(MtRadius.Lg))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Box(Modifier.bringIntoViewRequester(keTanggal)) {
                DateStrip(
                    terpilih = tanggal,
                    hariIni = hariIni,
                    tanggalBercatatan = tanggalBercatatan,
                    onPilih = onTanggal,
                    error = hasil?.errorField?.get(FieldForm.TANGGAL),
                    pemicuError = pemicuError
                )
            }
            Box(Modifier.bringIntoViewRequester(keWaktu)) {
                WaktuMakanPicker(
                    terpilih = waktu,
                    sudahDicatat = waktuSudahDicatat,
                    onPilih = onWaktu,
                    error = hasil?.errorField?.get(FieldForm.WAKTU),
                    pemicuError = pemicuError
                )
            }
            Box(Modifier.bringIntoViewRequester(keIsiPiring)) {
                BagianIsiPiring(
                    porsiTeks = porsiTeks,
                    onPorsi = onPorsi,
                    minumSuplemen = minumSuplemen,
                    onSuplemen = onSuplemen,
                    adaArahanSuplemen = adaArahanSuplemen,
                    onBukaProfil = onBukaProfil,
                    hasil = hasil,
                    pemicuError = pemicuError
                )
            }
            FieldCerita(cerita, onCerita)
            Row(Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TombolGaris("Reset", Icons.Rounded.Refresh, onReset)
                TombolUtama(
                    // Tombol menyebut tanggalnya saat Bunda mencatat untuk hari lain
                    if (tanggal == hariIni) "Simpan Catatan" else "Simpan untuk ${formatPendek(tanggal)}",
                    onSimpan,
                    Modifier.weight(1f),
                    ikon = Icons.Rounded.AddCircleOutline
                )
            }
        }
    }
}

/** Field teks opsional, dibatasi 100 karakter saat mengetik. */
@Composable
private fun FieldCerita(cerita: String, onCerita: (String) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Cerita Bunda (opsional)", style = isi(13, 800), color = MtColor.Ink, modifier = Modifier.weight(1f))
            Text("${cerita.length}/$CERITA_MAKS", style = isi(12, 700), color = MtColor.InkMuted)
        }
        Spacer(Modifier.height(8.dp))
        BasicTextField(
            value = cerita,
            onValueChange = { if (it.length <= CERITA_MAKS) onCerita(it) },
            textStyle = isi(14, 600).copy(color = MtColor.Ink),
            cursorBrush = SolidColor(MtColor.Primary),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { teks ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 84.dp)
                        .background(MtColor.Surface, RoundedCornerShape(MtRadius.Md))
                        .border(1.5.dp, MtColor.Line, RoundedCornerShape(MtRadius.Md))
                        .padding(16.dp)
                ) {
                    if (cerita.isEmpty()) {
                        Text(
                            "Contoh: hari ini mual berkurang setelah makan buah",
                            style = isi(14, 600),
                            color = MtColor.InkMuted
                        )
                    }
                    teks()
                }
            }
        )
    }
}

@Composable
private fun BagianIsiPiring(
    porsiTeks: Map<KelompokMakanan, String>,
    onPorsi: (KelompokMakanan, String?) -> Unit,
    minumSuplemen: Boolean,
    onSuplemen: (Boolean) -> Unit,
    adaArahanSuplemen: Boolean,
    onBukaProfil: () -> Unit,
    hasil: HasilValidasi?,
    pemicuError: Int
) {
    val jumlahDipilih = porsiTeks.size + if (minumSuplemen) 1 else 0
    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Isi piring", style = isi(13, 800), color = MtColor.Ink, modifier = Modifier.weight(1f))
            Text(
                "$jumlahDipilih kelompok dipilih",
                style = isi(11, 800),
                color = if (jumlahDipilih > 0) MtColor.Ink else MtColor.Primary,
                modifier = Modifier
                    .background(
                        if (jumlahDipilih > 0) MtColor.LimeMid else MtColor.PinkSoft,
                        RoundedCornerShape(MtRadius.Xs)
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
        Spacer(Modifier.height(12.dp))
        Text("Ketuk untuk memilih, lalu atur porsinya", style = isi(12, 600), color = MtColor.InkMuted)
        // Pesan diletakkan dekat judul agar terlihat bersama pesan Waktu makan
        FieldError(hasil?.errorField?.get(FieldForm.ISI_PIRING), pemicuError, Modifier.padding(top = 8.dp))
        Column(
            Modifier.padding(top = 22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            for (k in KelompokMakanan.entries) {
                IsiPiringPill(
                    gambar = k.gambar,
                    nama = k.label,
                    contoh = k.contoh,
                    warna = k.warna,
                    porsiTeks = porsiTeks[k],
                    onPorsiChange = { onPorsi(k, it) },
                    error = hasil?.errorPorsi?.get(k),
                    pemicuError = pemicuError
                )
            }
            SuplemenPill(dicentang = minumSuplemen, aktif = adaArahanSuplemen, onUbah = onSuplemen)
        }
        Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Info, null, tint = MtColor.InkMuted, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                if (adaArahanSuplemen) "Tidak diresepkan suplemen? " else "Suplemen tidak dihitung. ",
                style = isi(12, 700),
                color = MtColor.InkMuted
            )
            Text(
                "Atur di Profil",
                style = isi(12, 800),
                color = MtColor.Primary,
                modifier = Modifier.clickable(onClick = onBukaProfil)
            )
        }
    }
}
