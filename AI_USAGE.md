# Penggunaan AI

Dokumen ini mencatat secara jujur bagian proyek yang dibantu AI dan bagian yang dikerjakan/diperiksa anggota kelompok.

## Alat yang dipakai

| Alat | Dipakai untuk |
|---|---|
| Asisten AI untuk coding | Analisis ketentuan UTS, menyusun spesifikasi desain, membuat wireframe, menulis kode Kotlin/Compose, unit test, menguji di emulator, dan menyusun draf dokumentasi |
| Alat desain dengan bantuan AI | Wireframe dan ilustrasi flat (maskot Bunda, kelompok makanan, waktu makan, logo) yang diekspor menjadi PNG di `app/src/main/res/drawable-nodpi/` |

## Rincian

| Bagian | Peran AI | Peran anggota |
|---|---|---|
| Analisis kebutuhan | Merangkum ketentuan dari dokumen UTS, mengusulkan model data dan aturan validasi | Menentukan arah aplikasi dan menyetujui keputusan logika (catatan per waktu makan, porsi 1–5, pengaturan suplemen) |
| Wireframe dan desain visual | Membuat wireframe dan ilustrasi di alat desain dengan bantuan AI | Mengarahkan gaya visual dan meminta revisi berulang (warna, tata letak, navbar, umpan balik simpan) |
| Kode `model/` dan `logic/` | Menulis fungsi dan unit test | Meninjau, menjalankan test, dan wajib memahami seluruh logika untuk defense |
| Kode `ui/` | Menulis komponen Compose dan mengujinya di emulator | Mencoba aplikasi, memberi masukan, melakukan commit |
| Dokumentasi | Menyusun draf README, test case, dan dokumen ini | Melengkapi data anggota di CONTRIBUTIONS.md dan PRESENTATION.md, memeriksa isi |

## Contoh perintah ke AI

- "Bantu analisa bug ini dan mari kita perbaiki: kartu waktu makan yang dipilih tetap membesar setelah diketuk"
- "Mari benahi UI ini, konten tertutup status bar saat digulir"
- "Tab Riwayat yang diketuk malah menyorot Edukasi, tolong cek kenapa"
- "Strip tanggal yang dipilih pakai warna sky-mid, jangan ubah kartu waktu makan"
- "Tambahkan validasi porsi supaya huruf dan angka negatif tidak membuat aplikasi crash"

## Verifikasi oleh kelompok

- Seluruh kode dijalankan di emulator dan diuji dengan 14 skenario di `docs/test-cases.md`.
- 24 unit test dijalankan dengan `./gradlew testDebugUnitTest`.

## Batasan

AI tidak dipakai di dalam aplikasi. Aplikasi tidak memberi diagnosis, target gizi, atau dosis suplemen.
