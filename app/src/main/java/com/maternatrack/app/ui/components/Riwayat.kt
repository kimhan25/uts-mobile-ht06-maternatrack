package com.maternatrack.app.ui.components

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.logic.formatKartu
import com.maternatrack.app.model.CatatanMakan
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.RingkasanHarian
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.MtRadius
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul
import java.time.LocalDate

/** Judul bagian riwayat + chip jumlah catatan makan (ringkasan kedua). */
@Composable
fun JudulRiwayat(jumlahCatatan: Int) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("Riwayat Makan", style = judul(20), color = MtColor.Ink, modifier = Modifier.weight(1f))
        Row(
            Modifier
                .background(MtColor.Surface, RoundedCornerShape(MtRadius.Xs))
                .padding(start = 6.dp, end = 10.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(R.drawable.art_buku), null, Modifier.size(20.dp))
            Spacer(Modifier.width(4.dp))
            Text("$jumlahCatatan catatan", style = isi(12, 800), color = MtColor.Ink)
        }
    }
}

/** Kartu satu hari: cincin persentase, tanggal, lalu baris per waktu makan. */
@Composable
fun RiwayatHariCard(
    ringkasan: RingkasanHarian,
    hariIni: LocalDate,
    onDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(MtColor.Surface, RoundedCornerShape(MtRadius.Lg))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CincinProgres(ringkasan.persentase)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(formatKartu(ringkasan.tanggal, hariIni), style = judul(16), color = MtColor.Ink)
                Spacer(Modifier.height(2.dp))
                Text(
                    "${ringkasan.catatan.size} waktu makan dicatat",
                    style = isi(12, 700),
                    color = MtColor.InkMuted
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MtColor.Line)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (c in ringkasan.catatan) BarisWaktu(c)
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(MtRadius.Sm))
                .border(1.5.dp, MtColor.Line, RoundedCornerShape(MtRadius.Sm))
                .clickable(onClick = onDetail),
            contentAlignment = Alignment.Center
        ) {
            Text("Lihat detail", style = isi(13, 800), color = MtColor.Ink)
        }
    }
}

@Composable
private fun BarisWaktu(c: CatatanMakan) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(c.waktu.gambar), null, Modifier.size(28.dp))
        Spacer(Modifier.width(10.dp))
        Text(c.waktu.label, style = isi(13, 800), color = MtColor.Ink, modifier = Modifier.width(64.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            // Urutkan sesuai urutan enum agar tampilan konsisten
            for (k in KelompokMakanan.entries.filter { it in c.porsi }) {
                ThumbPorsi(k.gambar, k.warna.mid, c.porsi.getValue(k))
            }
            if (c.minumSuplemen) ThumbPorsi(Suplemen.GAMBAR, Suplemen.warna.mid, 1)
        }
    }
}

/** Thumbnail kelompok dengan badge porsi jika lebih dari 1. */
@Composable
fun ThumbPorsi(gambar: Int, latar: Color, porsi: Int, ukuran: Int = 30) {
    Box {
        Bubble(gambar, latar, ukuran = ukuran.dp)
        if (porsi > 1) {
            BadgePorsi(porsi, Modifier.align(Alignment.TopEnd).offset(x = 5.dp, y = (-5).dp))
        }
    }
}

/** Empty state saat belum ada catatan sama sekali. */
@Composable
fun EmptyRiwayat() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MtColor.Surface, RoundedCornerShape(MtRadius.Lg))
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(R.drawable.art_piring_kosong), null, Modifier.size(140.dp))
        Spacer(Modifier.height(12.dp))
        Text("Belum ada catatan makan", style = judul(20), color = MtColor.Ink)
        Spacer(Modifier.height(6.dp))
        Text(
            "Catatan makan Bunda akan muncul di sini, dikelompokkan per hari.",
            style = isi(13, 600),
            color = MtColor.InkMuted,
            textAlign = TextAlign.Center
        )
    }
}
