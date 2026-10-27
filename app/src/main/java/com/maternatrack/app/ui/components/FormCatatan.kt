package com.maternatrack.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maternatrack.app.logic.FieldForm
import com.maternatrack.app.logic.HasilValidasi
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
        }
    }
}
