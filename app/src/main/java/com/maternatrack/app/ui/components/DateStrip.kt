package com.maternatrack.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.logic.HARI_MUNDUR_MAKS
import com.maternatrack.app.logic.formatBulan
import com.maternatrack.app.logic.formatPanjang
import com.maternatrack.app.logic.hariPendek
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.MtRadius
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Field tanggal: strip 7 hari terakhir + tombol bulan yang membuka DatePickerDialog.
 * Kalender hanya mengizinkan hari ini sampai [HARI_MUNDUR_MAKS] hari sebelumnya.
 */
@Composable
fun DateStrip(
    terpilih: LocalDate,
    hariIni: LocalDate,
    tanggalBercatatan: Set<LocalDate>,
    onPilih: (LocalDate) -> Unit,
    error: String?,
    pemicuError: Int
) {
    var dialogTerbuka by remember { mutableStateOf(false) }

    // Strip selalu 7 lingkaran (muat di layar) berakhir di hari ini; batas yang lebih jauh lewat kalender
    val hari = (6L downTo 0L).map { hariIni.minusDays(it) }

    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Tanggal", style = isi(13, 800), color = MtColor.Ink, modifier = Modifier.weight(1f))
            Row(
                Modifier
                    .clip(RoundedCornerShape(MtRadius.Sm))
                    .background(MtColor.Surface)
                    .border(1.5.dp, MtColor.Line, RoundedCornerShape(MtRadius.Sm))
                    .clickable { dialogTerbuka = true }
                    .padding(start = 6.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painterResource(R.drawable.art_kalender), null, Modifier.size(24.dp))
                Spacer(Modifier.width(6.dp))
                Text(formatBulan(terpilih), style = isi(13, 800), color = MtColor.Ink)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Rounded.KeyboardArrowDown, null, tint = MtColor.InkMuted, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            for (t in hari) {
                HariItem(
                    tanggal = t,
                    dipilih = t == terpilih,
                    bercatatan = t in tanggalBercatatan,
                    onKlik = { onPilih(t) }
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Legenda(MtColor.SkyMid, "Dipilih")
            Legenda(MtColor.LimeMid, "Sudah dicatat")
            Legenda(MtColor.DayEmpty, "Belum dicatat")
        }
        // Penanda jelas saat Bunda mencatat untuk hari lain, dengan jalan pintas kembali ke hari ini
        AnimatedVisibility(
            visible = terpilih != hariIni,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Row(
                Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .background(MtColor.SkyTint, RoundedCornerShape(MtRadius.Sm))
                    .padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.EditCalendar, null, tint = MtColor.SkyDeep, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Mencatat untuk", style = isi(11, 700), color = MtColor.SkyDeep)
                    Text(formatPanjang(terpilih), style = isi(13, 800), color = MtColor.SkyDeep)
                }
                // Tautan teks tanpa wadah agar penanda tetap ringan; pembaca layar menyebut tujuannya lengkap
                Text(
                    "Kembali",
                    style = isi(13, 800),
                    color = MtColor.Primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(MtRadius.Xs))
                        .clickable(onClickLabel = "Kembali ke hari ini") { onPilih(hariIni) }
                        .semantics { contentDescription = "Kembali ke hari ini" }
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                )
            }
        }
        FieldError(error, pemicuError, Modifier.padding(top = 6.dp))
    }

    if (dialogTerbuka) {
        DialogTanggal(
            awal = terpilih,
            hariIni = hariIni,
            onPilih = { onPilih(it); dialogTerbuka = false },
            onTutup = { dialogTerbuka = false }
        )
    }
}

@Composable
private fun HariItem(
    tanggal: LocalDate,
    dipilih: Boolean,
    bercatatan: Boolean,
    onKlik: () -> Unit
) {
    val latar by animateColorAsState(
        when {
            dipilih -> MtColor.SkyMid
            bercatatan -> MtColor.LimeMid
            else -> MtColor.DayEmpty
        },
        label = "latarHari"
    )
    val warnaNama = when {
        dipilih -> MtColor.SkyDeep
        bercatatan -> MtColor.LimeDeep
        else -> MtColor.InkMuted
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(hariPendek(tanggal), style = isi(11, 800), color = warnaNama)
        Spacer(Modifier.height(6.dp))
        Box(
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(latar)
                .clickable(onClick = onKlik),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "${tanggal.dayOfMonth}",
                style = judul(17),
                color = if (dipilih || bercatatan) MtColor.Ink else MtColor.InkMuted
            )
        }
    }
}

@Composable
private fun Legenda(warna: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(12.dp).clip(CircleShape).background(warna))
        Spacer(Modifier.width(6.dp))
        Text(label, style = isi(11, 700), color = MtColor.InkMuted)
    }
}

// DatePicker bekerja dengan milidetik UTC, jadi tanggal dikonversi bolak-balik
private fun LocalDate.keMillis() = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
private fun Long.keTanggal() = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogTanggal(
    awal: LocalDate,
    hariIni: LocalDate,
    onPilih: (LocalDate) -> Unit,
    onTutup: () -> Unit
) {
    val paling = hariIni.minusDays(HARI_MUNDUR_MAKS)
    val batasAwal = paling.keMillis()
    val batasAkhir = hariIni.keMillis()
    val state = rememberDatePickerState(
        initialSelectedDateMillis = awal.keMillis(),
        yearRange = paling.year..hariIni.year,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis in batasAwal..batasAkhir
            override fun isSelectableYear(year: Int) = year in paling.year..hariIni.year
        }
    )
    val warna = DatePickerDefaults.colors(containerColor = MtColor.Surface)
    DatePickerDialog(
        onDismissRequest = onTutup,
        confirmButton = {
            TextButton(onClick = { state.selectedDateMillis?.let { onPilih(it.keTanggal()) } ?: onTutup() }) {
                Text("Pilih", style = isi(14, 800), color = MtColor.Primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onTutup) {
                Text("Batal", style = isi(14, 800), color = MtColor.InkMuted)
            }
        },
        colors = warna
    ) {
        // Judul dan tanggal terpilih ditulis sendiri agar tetap berbahasa Indonesia
        DatePicker(
            state = state,
            colors = warna,
            showModeToggle = false,
            title = {
                Text(
                    "Pilih tanggal makan",
                    style = isi(13, 700),
                    color = MtColor.InkMuted,
                    modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp)
                )
            },
            headline = {
                Text(
                    state.selectedDateMillis?.let { formatPanjang(it.keTanggal()) } ?: "-",
                    style = judul(24),
                    color = MtColor.Ink,
                    modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp)
                )
            }
        )
    }
}
