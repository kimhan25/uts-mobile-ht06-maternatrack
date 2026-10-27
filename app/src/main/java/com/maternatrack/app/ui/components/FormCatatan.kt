package com.maternatrack.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maternatrack.app.logic.FieldForm
import com.maternatrack.app.logic.HasilValidasi
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
    hasil: HasilValidasi?,
    pemicuError: Int
) {
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
            DateStrip(
                terpilih = tanggal,
                hariIni = hariIni,
                tanggalBercatatan = tanggalBercatatan,
                onPilih = onTanggal,
                error = hasil?.errorField?.get(FieldForm.TANGGAL),
                pemicuError = pemicuError
            )
            WaktuMakanPicker(
                terpilih = waktu,
                sudahDicatat = waktuSudahDicatat,
                onPilih = onWaktu,
                error = hasil?.errorField?.get(FieldForm.WAKTU),
                pemicuError = pemicuError
            )
            BagianIsiPiring(
                porsiTeks = porsiTeks,
                onPorsi = onPorsi,
                minumSuplemen = minumSuplemen,
                onSuplemen = onSuplemen,
                adaArahanSuplemen = adaArahanSuplemen,
                hasil = hasil,
                pemicuError = pemicuError
            )
        }
    }
}

@Composable
private fun BagianIsiPiring(
    porsiTeks: Map<KelompokMakanan, String>,
    onPorsi: (KelompokMakanan, String?) -> Unit,
    minumSuplemen: Boolean,
    onSuplemen: (Boolean) -> Unit,
    adaArahanSuplemen: Boolean,
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
        FieldError(hasil?.errorField?.get(FieldForm.ISI_PIRING), pemicuError, Modifier.padding(top = 10.dp))
    }
}
