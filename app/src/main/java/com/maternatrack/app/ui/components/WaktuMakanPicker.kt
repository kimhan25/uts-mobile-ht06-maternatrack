package com.maternatrack.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maternatrack.app.model.WaktuMakan
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.MtRadius
import com.maternatrack.app.ui.theme.isi

/**
 * Empat kartu waktu makan. Waktu yang sudah dicatat pada tanggal terpilih ditandai centang hijau
 * dan dikunci (kecuali Camilan yang boleh berulang).
 */
@Composable
fun WaktuMakanPicker(
    terpilih: WaktuMakan?,
    sudahDicatat: Set<WaktuMakan>,
    onPilih: (WaktuMakan) -> Unit,
    error: String?,
    pemicuError: Int
) {
    // Pesan saat kartu yang terkunci diketuk
    var pesanKunci by remember { mutableStateOf<String?>(null) }
    var pemicuKunci by remember { mutableIntStateOf(0) }

    Column {
        Text("Waktu makan", style = isi(13, 800), color = MtColor.Ink)
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (w in WaktuMakan.entries) {
                val terkunci = w in sudahDicatat && !w.bolehBerulang
                KartuWaktu(
                    waktu = w,
                    dipilih = w == terpilih,
                    sudah = w in sudahDicatat,
                    modifier = Modifier.weight(1f),
                    onKlik = {
                        if (terkunci) {
                            pesanKunci = "${w.label} tanggal ini sudah dicatat"
                            pemicuKunci++
                        } else {
                            pesanKunci = null
                            onPilih(w)
                        }
                    }
                )
            }
        }
        FieldError(error ?: pesanKunci, pemicuError + pemicuKunci, Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun KartuWaktu(
    waktu: WaktuMakan,
    dipilih: Boolean,
    sudah: Boolean,
    modifier: Modifier,
    onKlik: () -> Unit
) {
    val warna = waktu.warna
    // Camilan yang sudah dicatat tetap tampil seperti kartu biasa agar bisa dipilih lagi
    val tampilSudah = sudah && !dipilih
    val latar by animateColorAsState(
        when {
            dipilih -> warna.mid
            tampilSudah -> MtColor.LimeTint
            else -> MtColor.Surface
        },
        label = "latarWaktu"
    )
    val garis = when {
        dipilih -> warna.kuat
        tampilSudah -> MtColor.LimeMid
        else -> MtColor.Line
    }
    // Memantul sesaat ketika dipilih, lalu kembali ke ukuran normal
    val skala = remember { Animatable(1f) }
    LaunchedEffect(dipilih) {
        if (dipilih) {
            skala.snapTo(0.94f)
            skala.animateTo(1f, spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium))
        }
    }

    Box(modifier.scale(skala.value)) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(MtRadius.Md))
                .background(latar)
                .border(if (dipilih || tampilSudah) 2.dp else 1.5.dp, garis, RoundedCornerShape(MtRadius.Md))
                .clickable(onClick = onKlik),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painterResource(waktu.gambar), null, Modifier.size(44.dp))
            Spacer(Modifier.height(4.dp))
            Text(
                waktu.label,
                style = isi(12, 800),
                color = when {
                    dipilih -> MtColor.Ink
                    tampilSudah -> MtColor.LimeDeep
                    else -> MtColor.InkMuted
                }
            )
        }
        if (tampilSudah) {
            CentangHijau(
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 8.dp, y = (-11).dp)
            )
        }
    }
}
