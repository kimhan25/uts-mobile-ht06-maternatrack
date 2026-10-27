package com.maternatrack.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maternatrack.app.model.KelompokMakanan
import com.maternatrack.app.model.RingkasanHarian
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.MtRadius
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul

/**
 * Ringkasan hari ini: cincin persentase, jumlah komponen terpenuhi,
 * dan 6 bubble kelompok yang menyala jika sudah terpenuhi.
 */
@Composable
fun PiringHariIniCard(ringkasan: RingkasanHarian, adaArahanSuplemen: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .background(MtColor.Surface, RoundedCornerShape(MtRadius.Lg))
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CincinProgres(ringkasan.persentase)
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Piring hari ini", style = judul(17), color = MtColor.Ink)
                Spacer(Modifier.height(2.dp))
                Text(
                    "${ringkasan.jumlahTerpenuhi} dari ${ringkasan.jumlahKomponen} kelompok gizi terpenuhi",
                    style = isi(12, 700),
                    color = MtColor.InkMuted
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            for (k in KelompokMakanan.entries) {
                ItemKelompok(k.gambar, k.labelPendek, k.warna.mid, k in ringkasan.kelompokTerpenuhi)
            }
            if (adaArahanSuplemen) {
                ItemKelompok(Suplemen.GAMBAR, Suplemen.LABEL, Suplemen.warna.mid, ringkasan.suplemenTerpenuhi)
            }
        }
    }
}

@Composable
private fun ItemKelompok(gambar: Int, label: String, warnaMid: Color, menyala: Boolean) {
    val latar by animateColorAsState(if (menyala) warnaMid else MtColor.DayEmpty, label = "latar")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Bubble(gambar, latar, ukuran = 44.dp, ukuranGambar = 28.dp, redup = !menyala)
        Spacer(Modifier.height(6.dp))
        Text(
            label,
            style = isi(11, if (menyala) 800 else 700),
            color = if (menyala) MtColor.Ink else MtColor.InkMuted
        )
    }
}
