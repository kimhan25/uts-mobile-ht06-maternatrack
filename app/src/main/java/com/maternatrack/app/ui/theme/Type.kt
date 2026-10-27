package com.maternatrack.app.ui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.maternatrack.app.R

// Font variabel: satu file .ttf, ketebalan dipilih lewat FontVariation
@OptIn(ExperimentalTextApi::class)
private fun fontVariabel(id: Int, berat: Int) = Font(
    resId = id,
    weight = FontWeight(berat),
    variationSettings = FontVariation.Settings(FontVariation.weight(berat))
)

/** Fredoka untuk judul dan angka. */
val Fredoka = FontFamily(
    fontVariabel(R.font.fredoka, 500),
    fontVariabel(R.font.fredoka, 600),
    fontVariabel(R.font.fredoka, 700)
)

/** Nunito untuk teks isi. */
val Nunito = FontFamily(
    fontVariabel(R.font.nunito, 500),
    fontVariabel(R.font.nunito, 600),
    fontVariabel(R.font.nunito, 700),
    fontVariabel(R.font.nunito, 800)
)
