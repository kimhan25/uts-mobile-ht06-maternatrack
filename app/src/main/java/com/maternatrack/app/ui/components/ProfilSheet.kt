package com.maternatrack.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maternatrack.app.R
import com.maternatrack.app.ui.theme.MtColor
import com.maternatrack.app.ui.theme.isi
import com.maternatrack.app.ui.theme.judul

/** Lembar Profil Bunda: pengaturan arahan suplemen dan info tentang aplikasi. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilSheet(adaArahanSuplemen: Boolean, onUbahArahan: (Boolean) -> Unit, onTutup: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onTutup,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MtColor.Surface,
        dragHandle = { PegangSheet() }
    ) {
        Column(
            Modifier
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(R.drawable.art_logo), null, Modifier.size(52.dp))
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Profil Bunda", style = judul(22), color = MtColor.Ink)
                    Text("Atur pencatatan sesuai kondisi Bunda", style = isi(13, 700), color = MtColor.InkMuted)
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MtColor.SkyMid, RoundedCornerShape(20.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painterResource(R.drawable.art_suplemen), null, Modifier.size(64.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Saya mendapat arahan suplemen dari nakes", style = judul(16), color = MtColor.Ink)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Matikan jika tidak diresepkan. Suplemen tidak ikut dihitung dalam persentase.",
                        style = isi(12, 600),
                        color = MtColor.Ink
                    )
                }
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = adaArahanSuplemen,
                    onCheckedChange = onUbahArahan,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MtColor.SkyDeep,
                        checkedThumbColor = MtColor.Surface,
                        uncheckedTrackColor = MtColor.Surface,
                        uncheckedBorderColor = MtColor.InkMuted,
                        uncheckedThumbColor = MtColor.InkMuted
                    )
                )
            }

            HorizontalDivider(color = MtColor.Line)

            Column {
                Text("Tentang MaternaTrack", style = judul(16), color = MtColor.Ink)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Aplikasi edukasi untuk mencatat keragaman isi piring selama kehamilan. " +
                        "Data hanya simulasi dan tersimpan sementara. Bukan alat diagnosis dan tidak " +
                        "menentukan kebutuhan gizi atau dosis suplemen. Selalu ikuti arahan tenaga kesehatan.",
                    style = isi(14, 600),
                    color = MtColor.InkMuted
                )
            }

            TombolUtama("Mengerti", onTutup)
        }
    }
}
