package com.maternatrack.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.maternatrack.app.logic.PORSI_MAKS
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.MtRadius
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul

/**
 * Kartu kelompok makanan dengan porsi.
 * [porsiTeks] null = belum dipilih. Angka porsi bisa diketik, divalidasi saat Simpan.
 */
@Composable
fun IsiPiringPill(
    gambar: Int,
    nama: String,
    contoh: String,
    warna: WarnaKategori,
    porsiTeks: String?,
    onPorsiChange: (String?) -> Unit,
    error: String?,
    pemicuError: Int
) {
    val dipilih = porsiTeks != null
    Column {
        KerangkaPill(gambar, nama, contoh, warna, dipilih, onKlik = { if (!dipilih) onPorsiChange("1") }) {
            if (porsiTeks == null) {
                TombolBulat(Icons.Rounded.Add, warna.mid, warna.deep, 30, "Tambah $nama") { onPorsiChange("1") }
            } else {
                Stepper(porsiTeks, warna, onPorsiChange)
            }
        }
        FieldError(error, pemicuError, Modifier.padding(top = 6.dp))
    }
}

/** Kartu suplemen: tanpa porsi, cukup dicentang. Nonaktif jika Bunda tidak mendapat arahan suplemen. */
@Composable
fun SuplemenPill(dicentang: Boolean, aktif: Boolean, onUbah: (Boolean) -> Unit) {
    val warna = Suplemen.warna
    Box(Modifier.alpha(if (aktif) 1f else 0.5f)) {
        KerangkaPill(
            gambar = Suplemen.GAMBAR,
            nama = Suplemen.LABEL,
            contoh = if (aktif) "Sesuai arahan nakes" else "Tidak diresepkan",
            warna = warna,
            dipilih = dicentang && aktif,
            onKlik = { if (aktif) onUbah(!dicentang) }
        ) {
            if (aktif) KotakCentang(dicentang)
        }
    }
}

@Composable
private fun KerangkaPill(
    gambar: Int,
    nama: String,
    contoh: String,
    warna: WarnaKategori,
    dipilih: Boolean,
    onKlik: () -> Unit,
    aksi: @Composable () -> Unit
) {
    val latar by animateColorAsState(if (dipilih) warna.mid else MtColor.Surface, label = "latarPill")
    val garis by animateColorAsState(if (dipilih) warna.mid else MtColor.Line, label = "garisPill")
    val skala by animateFloatAsState(
        if (dipilih) 1.02f else 1f,
        spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium),
        label = "skalaPill"
    )
    val miring by animateFloatAsState(if (dipilih) -8f else 0f, spring(dampingRatio = 0.35f), label = "miring")

    Box(Modifier.fillMaxWidth().scale(skala)) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(68.dp)
                .clip(RoundedCornerShape(MtRadius.Md))
                .background(latar)
                .border(1.5.dp, garis, RoundedCornerShape(MtRadius.Md))
                .clickable(onClick = onKlik)
                .padding(start = 84.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(nama, style = judul(16), color = MtColor.Ink)
                Text(contoh, style = isi(12, 600), color = if (dipilih) MtColor.Ink else MtColor.InkMuted)
            }
            Spacer(Modifier.width(8.dp))
            aksi()
        }
        // Ilustrasi sedikit menyembul di atas kartu
        Image(
            painterResource(gambar),
            contentDescription = null,
            modifier = Modifier
                .offset(x = 4.dp, y = (-12).dp)
                .size(76.dp)
                .rotate(miring)
        )
    }
}

@Composable
private fun Stepper(porsiTeks: String, warna: WarnaKategori, onPorsiChange: (String?) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TombolBulat(Icons.Rounded.Remove, MtColor.Surface, MtColor.InkMuted, 28, "Kurangi porsi") {
            val n = porsiTeks.trim().toIntOrNull()
            // Mengurangi dari 1 berarti membatalkan pilihan kelompok ini
            onPorsiChange(
                when {
                    n == null -> "1"
                    n <= 1 -> null
                    else -> "${minOf(n - 1, PORSI_MAKS)}"
                }
            )
        }
        BasicTextField(
            value = porsiTeks,
            onValueChange = { onPorsiChange(it.take(3)) },
            singleLine = true,
            textStyle = judul(17).copy(color = MtColor.Ink, textAlign = TextAlign.Center),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(28.dp)
        )
        TombolBulat(Icons.Rounded.Add, MtColor.Surface, warna.deep, 28, "Tambah porsi") {
            val n = porsiTeks.trim().toIntOrNull()
            onPorsiChange(if (n == null || n < 1) "1" else "${minOf(n + 1, PORSI_MAKS)}")
        }
    }
}

@Composable
private fun TombolBulat(
    ikon: ImageVector,
    latar: Color,
    warnaIkon: Color,
    ukuran: Int,
    deskripsi: String,
    onKlik: () -> Unit
) {
    Box(
        Modifier.size(ukuran.dp).clip(CircleShape).background(latar).clickable(onClick = onKlik),
        contentAlignment = Alignment.Center
    ) {
        Icon(ikon, deskripsi, tint = warnaIkon, modifier = Modifier.size(18.dp))
    }
}

/** Kotak centang suplemen; centangnya "tergambar" seperti coretan krayon. */
@Composable
private fun KotakCentang(dicentang: Boolean) {
    val goresan by animateFloatAsState(if (dicentang) 1f else 0f, tween(300), label = "goresan")
    Box(
        Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(if (dicentang) MtColor.SkyMid else MtColor.Surface)
            .border(2.dp, if (dicentang) MtColor.SkyMid else MtColor.Line, RoundedCornerShape(9.dp))
    ) {
        Canvas(Modifier.size(28.dp)) {
            val k = size.width / 28f
            val centang = Path().apply {
                moveTo(6f * k, 14.5f * k)
                quadraticTo(9f * k, 17f * k, 11.5f * k, 20.5f * k)
                quadraticTo(15f * k, 12f * k, 22.5f * k, 6.5f * k)
            }
            val ukur = PathMeasure().apply { setPath(centang, false) }
            val sebagian = Path()
            ukur.getSegment(0f, ukur.length * goresan, sebagian, true)
            drawPath(sebagian, MtColor.SkyDeep, style = Stroke(3.6f * k, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}
