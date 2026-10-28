# Penggunaan AI

AI dipakai sebagai asisten selama pengerjaan. Arah aplikasi, keputusan desain dan logika, pengujian, serta pengumpulan tetap dikendalikan kelompok.

## Tabel Penggunaan AI (AI_USAGE.md)

| Tanggal | Alat AI | Prompt | Bagian digunakan | Modifikasi |
|---|---|---|---|---|
| 27 Oktober 2026 | Argon 4.0 | "Buatkan scaffolding awal proyek Android Jetpack Compose dengan Clean Architecture" | Generasi struktur folder, dependensi awal, dan boilerplate code | Menyesuaikan package name, menghapus modul yang tidak terpakai, dan menyelaraskan arsitektur proyek dengan kebutuhan kelompok |
| 27 Oktober 2026 | Argon 4.0 | "Membantu membuat wireframe dan ilustrasi aplikasi" | Desain dan ilustrasi aplikasi | Mengarahkan tampilan dan meminta revisi berulang sampai sesuai (navbar, notifikasi simpan, warna, pop-up, ikon aplikasi) |
| 27 Oktober 2026 | Argon 4.0 | "Bantu setup dependensi Hilt untuk Dependency Injection di proyek Compose" | Modifikasi file build.gradle dan Application class | Menambahkan anotasi @AndroidEntryPoint pada MainActivity dan menyambungkan dependensi ViewModel |
| 27 Oktober 2026 | Argon 4.0 | "Bagaimana cara setup Room Database untuk menyimpan data porsi makan secara lokal?" | Menulis entitas, DAO, dan konfigurasi database Room | Mengubah skema tabel agar sesuai dengan model data kelompok dan menguji query penyimpanan lokal |
| 27 Oktober 2026 | Argon 4.0 | "Bantu implementasi Jetpack DataStore untuk menyimpan preferensi tema" | Menulis kode implementasi DataStore Preferences | Menghubungkan DataStore dengan ViewModel utama dan memastikan pergantian tema berjalan mulus di emulator |
| 27 Oktober 2026 | Argon 4.0 | "Buatkan kerangka Navigation Compose untuk pindah dari layar Beranda ke layar Detail Makanan" | Menulis kode Kotlin/Compose untuk sistem navigasi | Menyesuaikan rute navigasi dengan arsitektur proyek dan menambahkan argumen untuk passing data antar layar |
| 27 Oktober 2026 | Argon 4.0 | "Buatkan komponen Bottom Navigation Bar dengan animasi ketika tab aktif" | Menulis komponen UI Compose | Mengganti ikon bawaan dengan aset lokal dan menyesuaikan routing navigasinya |
| 27 Oktober 2026 | Argon 4.0 | "Buatkan dummy data JSON berisi daftar menu makanan untuk keperluan testing UI" | Struktur data JSON dan parsing lokal | Memasukkan dummy data ke dalam aplikasi untuk menguji performa list sebelum memakai data asli |
| 27 Oktober 2026 | Argon 4.0 | "Bantu terapkan Coroutines untuk proses pemuatan data yang berat agar UI tidak terblokir" | Menulis fungsi suspend dan penanganan dispatcher IO | Memastikan pembatalan coroutine saat ViewModel dihancurkan untuk mencegah memory leak |
| 27 Oktober 2026 | Argon 4.0 | "Berikan contoh penggunaan StateFlow di ViewModel untuk menggantikan LiveData" | Refaktor dari LiveData ke StateFlow | Menguji ulang semua observasi state di UI agar tidak ada memory leak saat pergantian layar |
| 28 Oktober 2026 | Argon 4.0 | "Bantu implementasi AlarmManager untuk memunculkan notifikasi pengingat waktu makan" | Menulis kode BroadcastReceiver dan NotificationManager | Mengatur perizinan notifikasi di AndroidManifest dan memastikan notifikasi muncul tepat waktu di perangkat uji |
| 28 Oktober 2026 | Argon 4.0 | "Bantu buatkan animasi transisi 'fade in' yang halus saat memunculkan popup notifikasi" | Menulis kode animasi Compose (AnimatedVisibility) | Menyesuaikan durasi animasi dan melakukan pengetesan visual agar tidak mengganggu UX |
| 28 Oktober 2026 | Argon 4.0 | "Bagaimana cara membuat custom modifier di Compose untuk efek klik dengan ripple warna kustom?" | Menulis custom modifier di Kotlin | Mengaplikasikan modifier tersebut ke semua tombol utama aplikasi dan menyesuaikan radius ripple |
| 28 Oktober 2026 | Argon 4.0 | "Bantu desainkan beberapa alternatif ikon aplikasi dengan tema kesehatan dan warna pastel" | Konsep desain dan variasi ikon vektor | Kelompok memilih satu desain yang paling cocok, lalu meminta revisi penyesuaian kontras warna |
| 28 Oktober 2026 | Argon 4.0 | "Tuliskan regex untuk memvalidasi input nama pengguna agar tidak mengandung karakter khusus" | Menyusun fungsi regex validasi | Memasukkan ke dalam util function kelompok dan menambah skenario unit test untuk regex tersebut |
| 28 Oktober 2026 | Argon 4.0 | "Bagaimana cara mengoptimalkan LazyColumn agar scrolling list makanan yang panjang tidak lag?" | Menulis kode optimasi dengan key pada item LazyColumn | Melakukan profiling memori di Android Studio dan menghapus recomposition yang tidak perlu |
| 28 Oktober 2026 | Argon 4.0 | "Refactor fungsi Composable KartuMakan ini karena terlalu panjang, tolong pecah jadi komponen kecil" | Menulis ulang dan merapikan struktur kode UI Compose | Memeriksa kembali alur state hoisting agar tidak ada bug pada pembaruan UI |
| 28 Oktober 2026 | Argon 4.0 | "Mari benahi UI ini, konten tertutup status bar saat digulir" | Menulis kode Kotlin/Compose, unit test, dan draf dokumentasi sesuai arahan kelompok | Memahami seluruh kode untuk defense dan live modification, mencoba setiap perubahan di emulator, menemukan bug, dan memutuskan apa yang masuk ke commit |
| 28 Oktober 2026 | Argon 4.0 | "Tambahkan validasi porsi supaya huruf dan angka negatif tidak membuat aplikasi crash" | Menulis kode Kotlin/Compose, unit test, dan draf dokumentasi sesuai arahan kelompok | Memahami seluruh kode untuk defense dan live modification, mencoba setiap perubahan di emulator, menemukan bug, dan memutuskan apa yang masuk ke commit |
| 28 Oktober 2026 | Argon 4.0 | "Bantu analisa bug ini dan mari kita perbaiki: kartu waktu makan yang dipilih tetap membesar setelah diketuk" | Menulis kode Kotlin/Compose, unit test, dan draf dokumentasi sesuai arahan kelompok | Memahami seluruh kode untuk defense dan live modification, mencoba setiap perubahan di emulator, menemukan bug, dan memutuskan apa yang masuk ke commit |
| 28 Oktober 2026 | Argon 4.0 | "Tuliskan Unit Test memakai JUnit4 untuk fungsi perhitungan total asupan di ViewModel" | Menulis skenario unit test dan mock data | Meninjau coverage pengujian, memperbaiki asersi yang keliru, dan memastikan test lolos secara lokal |

## Verifikasi oleh kelompok

- Aplikasi diuji di emulator dengan 21 skenario di `docs/test-cases.md`.
- 25 unit test dijalankan dengan `./gradlew testDebugUnitTest`.

## Batasan

AI tidak dipakai di dalam aplikasi. Aplikasi tidak memberi diagnosis, target gizi, atau dosis suplemen.
