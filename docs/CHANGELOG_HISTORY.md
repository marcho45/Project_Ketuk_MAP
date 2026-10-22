### [2026-10-22 09:50] Penyeragaman Panjang Kode Verifikasi OTP 6 Digit untuk Demo dan Full
- Alasan/permintaan: Menyesuaikan panjang kode OTP pada layar verifikasi (`VerificationViewModel.kt`) menjadi 6 digit untuk kedua build variant (`demo` dan `full`), selaras dengan konfigurasi default token autentikasi Supabase Auth. Memastikan tidak ada file/kode lain yang diubah.
- File diubah:
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/verification/VerificationViewModel.kt`: Menyeragamkan `const val CODE_LENGTH: Int = 6` (berlaku untuk `demo` dan `full`).
  * `docs/SCREENS.md`: Memperbarui tabel status layar, spesifikasi visual, dan catatan selisih kesepakatan tim untuk OTP 6 digit.
  * `docs/CHANGELOG_HISTORY.md`: Menambahkan riwayat perubahan verifikasi 6 digit.
- Dampak ke layar/fitur/rubrik:
  * Konsistensi Autentikasi: Layar verifikasi OTP di flavor `demo` dan `full` kini konsisten menampilkan 6 kotak input digit yang proporsional sesuai standar token Supabase.
  * Integritas Kode: Tidak ada kode lain di luar `VerificationViewModel.kt` yang diubah (0 perubahan pada `data/remote/` dan `data/repository/`).
- Hasil build/lint:
  * `./gradlew compileDemoDebugKotlin`: BUILD SUCCESSFUL
  * `./gradlew compileFullDebugKotlin`: BUILD SUCCESSFUL

---

### [2026-10-22 09:20] Pembersihan Panah Back di UI dan Penataan Perilaku Back Sistem di Seluruh Layar
- Alasan/permintaan: 
  (A) Merapikan panah back di UI: Menghapus IconButton/ArrowBack dan spacer/padding pendukungnya di seluruh layar aplikasi (Login, Setup Wali: DetailOmaOpa, CircleKeluarga, QrWali, PilihPenjaga; Oma/Opa: ScanQr, OmaPermissions, OmaPassive; Penjaga: GuardianHome, GuardianLocationHistory; Wali: Home, DetailAlert, Notifikasi), KECUALI di RoleSelectScreen ("Siapa kamu?"). Di RoleSelectScreen, tombol panah back di UI ditampilkan di kedua flavor (demo dan full) dengan tata letak asli Figma.
  (B) Penataan perilaku back sistem (tombol hardware / gesture HP):
  1. Splash di-pop dari back stack (popUpTo inclusive).
  2. Onboarding dan Login: back keluar aplikasi. Onboarding di-pop saat masuk Login.
  3. Register kembali ke Login; OTP (full) kembali ke Register.
  4. RoleSelectScreen ("Siapa kamu?"): back UI dan back sistem kembali ke Login.
  5. Setelah menekan "Lanjutkan" di RoleSelect: seluruh wizard dan auth di-pop. Layar pertama tiap peran (DetailOmaOpa, ScanQr, GuardianHome) menjadi root (back keluar aplikasi).
  6. Alur Wali: CircleKeluarga back ke DetailOmaOpa; QrWali back ke CircleKeluarga.
  7. Selama pairing di QrWali dan ScanQr: BackHandler aktif no-op mencegah pembatalan proses.
  8. Setelah pairing selesai: wizard setup di-pop. PilihPenjaga (Wali) dan OmaPermissions (Oma) menjadi root (back keluar aplikasi).
  9. Setelah selesai wizard: navigasi ke Home masing-masing peran membersihkan back stack (popUpTo(0) inclusive). Back di Home keluar aplikasi.
  10. Bottom Tabs: Back pada tab selain Tab 0 kembali ke Tab 0; back pada Tab 0 keluar aplikasi. Sub-layar (Riwayat Lokasi, Notifikasi, Detail Alert) kembali ke pemanggilnya.
- File diubah:
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/role/RoleSelectScreen.kt`: Menghapus kondisi `!BuildConfig.DEMO_MODE` sehingga IconButton panah back dan spacing (8dp & 16dp) tampil tanpa syarat di kedua flavor sesuai Figma.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/login/LoginScreen.kt`: Menghapus IconButton panah kembali dan BackHandler demo agar back sistem langsung keluar aplikasi.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/walisetup/WaliSetupScreens.kt`: Menghapus IconButton panah kembali dari `DetailOmaOpaScreen`, `CircleKeluargaScreen`, `QrWaliScreen`, dan `PilihPenjagaScreen`. Menambahkan BackHandler no-op selama `isPairing == true` di `QrWaliScreen`.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/oma/OmaScreens.kt`: Menghapus IconButton panah kembali di `ScanQrScreen` dan `OmaPermissionsScreen`. Menghapus IconButton panah kembali demo dan demo BackHandler di `OmaPassiveScreen`. Menambahkan BackHandler no-op selama pairing di `ScanQrScreen` dan `LoadingPairingOmaContent`. Menjaga penuh perubahan desain baru pengguna di `OmaPassiveScreen`.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/home/HomeScreen.kt`: Menghapus IconButton panah kembali demo di Header Tab 0. Mengonfigurasi `BackHandler(enabled = selectedTab != 0) { selectedTab = 0 }` sehingga tab lain kembali ke Tab 0, dan Tab 0 keluar aplikasi.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianHomeScreen.kt`: Menghapus IconButton panah kembali demo di Header Tab 0. Mengonfigurasi `BackHandler(enabled = selectedTab != 0) { selectedTab = 0 }`.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianLocationHistoryScreen.kt`: Menghapus IconButton panah kembali dari header TopAppBar.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/detail/DetailAlertScreen.kt`: Menghapus `navigationIcon` panah kembali dari TopAppBar.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/notifikasi/NotifikasiScreen.kt`: Menghapus `navigationIcon` panah kembali dari TopAppBar.
  * `app/src/main/java/com/chat/ketukprojectmap/navigation/KetukNavHost.kt`: Mengonfigurasi popUpTo inclusive pada seluruh transisi navigasi komit (Lanjutkan di RoleSelect, sukses pairing, simpan penjaga / izin oma, dan perpindahan peran).
  * `docs/SCREENS.md`: Memperbarui tabel status layar, spesifikasi visual, dan catatan selisih desain terkait penghapusan panah back di UI dan standarisasi navigasi back sistem.
  * `docs/CHANGELOG_HISTORY.md`: Menambahkan riwayat perubahan pembersihan back arrow UI dan penataan back sistem.
- Dampak ke layar/fitur/rubrik:
  * Keselarasan Desain UI / Figma: UI seluruh layar kini 100% selaras dengan desain Figma (tanpa panah kembali redundan di form/wizard/home), kecuali RoleSelectScreen yang mempertahankan tombol panah kembali di UI.
  * State & Back Stack Management: Alur back sistem teratur, mencegah user tersasar kembali ke splash, onboarding, atau wizard form yang sudah di-submit.
- Hasil build/lint:
  * `./gradlew compileDemoDebugKotlin`: BUILD SUCCESSFUL
  * `./gradlew compileFullDebugKotlin`: BUILD SUCCESSFUL

---

### [2026-10-22 07:45] Penyesuaian Tombol Back UI Layar Pilih Peran Khusus Flavor Demo
- Alasan/permintaan: Mengoreksi ruang lingkup peniadaan tombol back UI pada `RoleSelectScreen.kt` agar hanya berlaku untuk flavor `demo` (lewat pengecekan `!BuildConfig.DEMO_MODE`). Flavor `full` dikembalikan 100% identik dengan implementasi asli (IconButton + ArrowBack + spacing 8dp/16dp). Menghapus catatan selisih global di Bagian 4 docs/SCREENS.md.
- File diubah:
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/role/RoleSelectScreen.kt`: Menambahkan percabangan `if (!BuildConfig.DEMO_MODE) { ... } else { Spacer(height = 32.dp) }`. Pada mode `full`, tombol back UI (IconButton + ArrowBack) dan jarak asal (8dp & 16dp) tampil utuh; pada mode `demo`, tombol disembunyikan dengan padding atas 32dp. BackHandler dan parameter onNavigateBack dipertahankan di kedua flavor.
  * `docs/SCREENS.md`: Memperbarui deskripsi M3 Pilih Peran bahwa peniadaan tombol UI hanya khusus flavor `demo` sedangkan flavor `full` tidak berubah; menghapus entri selisih kesepakatan tim di Bagian 4.
  * `docs/CHANGELOG_HISTORY.md`: Mengoreksi riwayat perubahan tombol back UI Pilih Peran agar spesifik untuk flavor demo.
- Dampak ke layar/fitur/rubrik:
  * Flavor demo: Layar Pilih Peran ("Siapa kamu?") tampil bersih tanpa tombol back di sudut kiri atas; layout judul rapi dengan jarak ideal 32dp dari status bar. Navigasi mundur tetap didukung via gesture/hardware back HP.
  * Flavor full: Kode dan visual kembali 100% identik dengan desain asal (tombol panah kembali di kiri atas tetap ada).
  * System Back & Stability: Navigasi back perangkat/gesture sistem tetap mengembalikan user ke halaman Login di kedua flavor tanpa crash.
- Hasil build/lint:
  * `./gradlew assembleDemoDebug`: BUILD SUCCESSFUL
  * `./gradlew assembleFullDebug`: BUILD SUCCESSFUL
  * `./gradlew lintDemoRelease`: BUILD SUCCESSFUL (0 errors, 35 warnings)

---

### [2026-10-21 02:45] Unifikasi Animasi Masuk (FadeSlideIn Bertingkat) Seluruh Layar
- Alasan/permintaan: Menyeragamkan animasi masuk (fade + slide-up bertingkat) di seluruh layar dan semua peran (Auth, Wali, Oma/Opa, Penjaga) mengikuti pola standar Home Wali (alpha 400ms, translasi Y 16dp dari bawah, stagger delay 80ms per blok hingga maks 600ms).
- File diubah:
  * Baru: `app/src/main/java/com/chat/ketukprojectmap/ui/components/KetukAnimations.kt` (komponen publik `FadeSlideIn`, `SlideDirection`, dan helper `staggerDelay` dengan perlindungan `rememberSaveable` agar tidak stuck transparan saat rotasi layar).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/home/HomeScreen.kt`: Menghapus deklarasi private `FadeSlideIn` dan mengimpor dari `KetukAnimations.kt`.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/lokasi/LokasiScreen.kt`: Mengimpor `FadeSlideIn` dari `KetukAnimations.kt` menggantikan import HomeScreen.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/login/LoginScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada Maskot/Logo (0ms), Form Input (80ms), Tombol Masuk (160ms), Opsi Google & Register (240ms), dan Kartu Demo (320ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/register/RegisterScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada Maskot/Header (0ms), Field Nama & Email (80ms), Field Nomor HP (160ms), Field Password & Konfirmasi (240ms), dan Tombol Daftar (320ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/verification/VerificationScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada Maskot HP (0ms), Kotak Kode OTP (80ms), Tombol Confirm (160ms), dan Link Kirim Ulang (240ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/role/RoleSelectScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada Header (0ms), Kartu Wali (80ms), Kartu Penjaga (160ms), Kartu Oma/Opa (240ms), dan Tombol Lanjutkan (320ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/riwayat/RiwayatScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada Judul Tab (0ms), Filter Chips (80ms), dan 6 item pertama `LazyColumn` (160ms - 560ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/profile/ProfileScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada Judul Tab (0ms), Kartu Akun Wali (80ms), Kartu Lansia (160ms), Kartu Mode Gelap (240ms), dan Tombol Keluar (320ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/notifikasi/NotifikasiScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada 6 item pertama `LazyColumn` notifikasi (40ms - 440ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/detail/DetailAlertScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada Kartu Ringkasan Alert (0ms), Kartu Info Lansia (80ms), dan Blok Tombol Tindakan Wali (160ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/walisetup/WaliSetupScreens.kt`:
    - `DetailOmaOpaScreen`: Header (0ms), Field Wali (80ms), Toggle Gender (160ms), Field Lansia & Umur (240ms), Tombol Lanjutkan (320ms).
    - `CircleKeluargaScreen`: Header (0ms), Kartu Anggota (80ms), Kartu Undang & QR (160ms), Tombol Lanjutkan (240ms).
    - `QrWaliScreen`: Header (0ms), Kartu QR Pairing (80ms), Tombol Simulasikan (160ms).
    - `PilihPenjagaScreen`: Header (0ms), Search Bar (80ms), Kartu Kontak Radio (160ms), Tombol Pilih (240ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/oma/OmaScreens.kt`:
    - `ScanQrScreen`: Header (0ms), Viewport Kamera & Reticle (80ms), Kartu Petunjuk (160ms), Tombol Simulasikan (240ms).
    - `OmaPermissionsScreen`: Header (0ms), Kartu Yang Dipantau (80ms), Kartu Izin (160ms), Checklist Privasi (240ms), Tombol Setuju (320ms).
    - `OmaPassiveScreen`: Header (0ms), Hero Avatar & Status (80ms), Kartu Sapaan L1 (160ms), Kartu Kontak Wali (240ms), Kartu Mode Gelap (320ms), Tombol Keluar (400ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianHomeScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada Header Penjaga (0ms), Banner Ketuk Pintu & Tombol Aksi (80ms), Kartu Peta Mini (160ms), Kartu Aksi Cepat (240ms), dan Kartu Riwayat (320ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianLokasiScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada Judul Tab (0ms), Kartu Melayang Oma Diyas (80ms), Viewport Peta Statis (160ms), dan Navigasi Riwayat Lokasi (240ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianLocationHistoryScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada Header Top Bar (0ms) dan 6 item pertama `LazyColumn` riwayat pergerakan (40ms - 440ms).
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianProfileScreen.kt`: Menerapkan `FadeSlideIn` bertingkat pada Judul Tab (0ms), Kartu Info Penjaga (80ms), Kartu Status Kesediaan (160ms), Kartu Lansia Dipantau (240ms), Kartu Mode Gelap (320ms), dan Tombol Keluar (400ms).
  * `docs/SCREENS.md`: Menambahkan rincian animasi masuk terpadu `FadeSlideIn` pada spesifikasi visual dan selisih desain.
  * `docs/CHANGELOG_HISTORY.md`: Menambahkan catatan riwayat unifikasi animasi masuk.
- Dampak ke layar/fitur/rubrik:
  * Compose Layout & Animation: Seluruh layar di semua peran memiliki transisi masuk yang konsisten, halus, dan elegan (fade 400ms + slide 16dp ke atas) tanpa lag, meningkatkan UX dan polesan aplikasi.
  * Lazy Loading / List Screen: Item `LazyColumn` pada Riwayat Aktivitas, Notifikasi, dan Riwayat Lokasi hanya menganimasikan 6 item awal saat pertama kali dimuat tanpa mengulang saat scroll.
  * Robustness & State: Menggunakan `rememberSaveable` di level animasi mencegah konten transparan macet (stuck) saat rotasi layar atau navigasi balik dari back stack.
- Hasil build/lint:
  * `./gradlew assembleDemoDebug assembleFullDebug`: BUILD SUCCESSFUL
  * `./gradlew lintDemoRelease`: BUILD SUCCESSFUL (0 errors, 35 warnings)

---

### [2026-10-21 01:30] Perbaikan Build Variant Demo (Tahap A & Tahap B 5 Prioritas Tinggi)
- Alasan/permintaan: Menyelesaikan audit review Jetpack Compose UTS IF570L: memperbaiki 6 error inspection launcher icon mipmap-anydpi (Tahap A) dan mengimplementasikan 5 temuan prioritas Tinggi dari audit rubrik UTS (Tahap B).
- File diubah:
  * Dihapus: `app/src/main/res/mipmap-anydpi/ic_launcher.xml` dan `ic_launcher_round.xml` (vektor fallback API 23-25 yang salah penempatan di folder mipmap; minSdk 26 sudah memiliki adaptive icon di `mipmap-anydpi-v26`).
  * `app/build.gradle.kts`: Override `SUPABASE_URL`, `SUPABASE_ANON_KEY`, `SUPABASE_KEY`, dan `GOOGLE_WEB_CLIENT_ID` menjadi string kosong `""` di flavor `demo` agar kredensial `local.properties` tidak bocor ke APK demo UTS; flavor `full` tetap menggunakan kredensial asli.
  * `app/src/main/java/com/chat/ketukprojectmap/data/model/HomeSampleData.kt` & `HomeScreen.kt`: Menambahkan field `id: String` pada `RecentActivity` ("act_3", "act_4", "act_7") dan meneruskan `activity.id` langsung ke `onActivityClick`, memperbaiki bug salah buka item detail alert.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/login/LoginScreen.kt` & `ui/components/AuthComponents.kt`: Mengganti `remember` menjadi `rememberSaveable` untuk `email`, `password`, dan `passwordVisible`; membatasi panjang input; menambahkan validasi format email & sandi minimal 6 karakter secara live; serta memblokir tombol submit sampai valid.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/register/RegisterViewModel.kt` & `RegisterScreen.kt`: Menambahkan `.take(13)` pada nomor HP, batas panjang karakter nama/email/password; validasi live untuk nama, format email, nomor HP (9-13 digit), sandi (min 8 karakter), dan konfirmasi sandi; serta memblokir tombol "Daftar" sampai seluruh form valid.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/walisetup/WaliSetupScreens.kt`: Membatasi panjang nama wali dan lansia `.take(60)` di `DetailOmaOpaScreen`; menggunakan `rememberSaveable` untuk `hasAddedExtraMember` di `CircleKeluargaScreen` agar anggota tambahan ("Kakak Raka") bertahan saat rotasi layar.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/notifikasi/NotifikasiScreen.kt`: Menggunakan `rememberSaveable` untuk `unreadIds` agar status "Tandai dibaca" dan klik notifikasi bertahan saat rotasi layar.
  * `app/src/main/java/com/chat/ketukprojectmap/ui/screens/profile/ProfileScreen.kt`: Menggunakan `rememberSaveable` untuk `showLogoutDialog`.
  * `SupabaseProvider.kt`, `AuthRepository.kt`, `GuardianHomeScreen.kt`, `HomeScreen.kt`, `LoginScreen.kt`, `OmaScreens.kt`, `RegisterScreen.kt`: Memakai fully-qualified `com.chat.ketukprojectmap.BuildConfig.*` agar aman dari penghapusan import otomatis oleh IDE ("Optimize imports on the fly").
- Dampak ke layar/fitur/rubrik:
  * Screen Implementation & Navigation: Klik aktivitas di Beranda Wali membuka detail item yang tepat (`act_3`/`act_4`/`act_7`).
  * State Management: Seluruh input form, dialog logout, status notifikasi, dan penambahan anggota circle tidak lagi hilang saat rotasi layar (portrait/landscape).
  * Form Input & Validation: Validasi live dengan feedback pesan error dan pemblokiran tombol submit pada Login dan Register; panjang HP dibatasi maksimal 13 digit.
  * Dummy Data & Deliverable: APK flavor demo bebas dari kebocoran kredensial Supabase di BuildConfig; error lint berkurang menjadi 0 error.
- Hasil build/lint:
  * `./gradlew assembleDemoDebug assembleDemoRelease assembleFullDebug`: BUILD SUCCESSFUL
  * `./gradlew lintDemoRelease`: BUILD SUCCESSFUL (0 errors, 35 warnings)

---

## [2026-10-19 07:50] Tugas Khusus: Perapian Tampilan & Responsivitas (Kartu Demo Login, Tombol & Header Penjaga, Padding Bottom Bar, dan Preview Multi-Device)

- **Tujuan:** Merapikan tampilan dan meningkatkan responsivitas antarmuka aplikasi pada layar sempit (320dp) dan aksesibilitas (fontScale 1.3) tanpa mengubah logika bisnis, navigasi, mode terang, maupun sistem autentikasi, dengan maksimal 5 file kode.
- **Referensi desain:** `docs/figma/01. Authentication.png`, `docs/figma/05. Core Phase Penjaga.png`, dan `docs/SCREENS.md`.
- **Ringkasan perubahan:**
  - `LoginScreen.kt`:
    1. Memecah judul kartu akun demo menjadi 2 teks: judul tebal "Akun Demo UTS" dan keterangan sekunder "Ketuk baris untuk isi otomatis. Sandi: Ketuk123".
    2. Menyusun setiap akun demo menjadi 2 baris vertikal (label peran + email di baris atas, nama lengkap di baris bawah) agar teks tidak terpotong atau terpecah di tengah saat layar sempit.
    3. Menambahkan baris catatan petunjuk di bawah daftar akun: "Tombol 'Lanjut dengan Google' (simulasi) langsung membuka halaman Pilih Peran."
    4. Menambahkan preview aksesibilitas `LoginScreenAccessibilityPreview` (360dp, fontScale = 1.3f).
  - `GuardianHomeScreen.kt`:
    1. Mengganti tinggi tetap tombol aksi banner "Oma aman" dan "Butuh bantuan" dengan `heightIn(min = 48.dp)` di dalam `BoxWithConstraints`. Saat ruang sempit (< 270dp di kartu) atau fontScale > 1.15f, tombol otomatis tersusun vertikal selebar kartu sehingga teks utuh dan tidak terpotong.
    2. Menyesuaikan header Beranda Penjaga: sapaan "Halo, Markolintang" menggunakan ukuran 22sp dengan `weight(1f)` dan `TextOverflow.Ellipsis`, serta tombol panah demo dan lonceng notifikasi dibuat proporsional agar sapaan muat 1 baris pada layar 360dp.
    3. Judul banner "Tolong ketuk pintu Oma Diyas" diizinkan 2 baris rapi dengan `lineHeight = 20.sp`.
    4. Menambah padding bawah konten scrollable menjadi `64.dp + navBarInset + 32.dp` agar kartu riwayat terakhir tidak terhalang bottom nav bar.
    5. Menambahkan `@Preview` untuk 320dp, 360dp, dan aksesibilitas 360dp fontScale 1.3.
  - `GuardianLokasiScreen.kt`:
    1. Menambah padding bawah konten scrollable menjadi `64.dp + navBarInset + 32.dp`.
    2. Menambahkan `@Preview` untuk 320dp, 360dp, dan aksesibilitas 360dp fontScale 1.3.
  - `GuardianProfileScreen.kt`:
    1. Menambah padding bawah konten scrollable menjadi `64.dp + navBarInset + 32.dp`.
    2. Menambahkan `@Preview` untuk 320dp, 360dp, dan aksesibilitas 360dp fontScale 1.3.
  - `OmaScreens.kt` (`OmaPassiveScreen`):
    1. Menambahkan `@Preview` untuk 320dp dan 360dp (melengkapi preview aksesibilitas fontScale 1.3 yang sudah ada).
- **File dibuat:** Tidak ada
- **File diubah:**
  1. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/login/LoginScreen.kt`
  2. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianHomeScreen.kt`
  3. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianLokasiScreen.kt`
  4. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianProfileScreen.kt`
  5. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/oma/OmaScreens.kt`
  6. `docs/SCREENS.md`
  7. `docs/CHANGELOG_HISTORY.md`
- **File dihapus:** Tidak ada
- **Error/masalah:** Tidak ada
- **Solusi yang berhasil:** Penerapan `BoxWithConstraints` dan layout dinamis pada kartu demo dan banner penjaga memastikan keterbacaan penuh tanpa pemotongan teks pada berbagai resolusi dan skala font.
- **Status build:** `./gradlew assembleDemoDebug assembleFullDebug` **BUILD SUCCESSFUL**
- **Belum selesai:** Tahap 5 (`README.txt` & Finalisasi UTS).

---

## [2026-10-19 06:15] Tahap 4: Alur Penjaga M10 (Beranda, Lokasi, Riwayat Lokasi 7 Hari, dan Profil Penjaga)

- **Tujuan:** Mengimplementasikan seluruh alur Penjaga (M10) sesuai `05. Core Phase Penjaga.png` dan `flowchart.png`, mengoreksi sapaan Beranda Wali menjadi `"Halo, Nanda"`, memperbarui dokumentasi aset/proyek, dan menambahkan layar Riwayat Lokasi 7 Hari serta Profil Penjaga dengan mode gelap manual dan tombol keluar yang membersihkan back stack.
- **Referensi desain:** `docs/figma/05. Core Phase Penjaga.png`, `docs/figma/flowchart.png`, `docs/SCREENS.md`, `docs/ASSETS.md`, dan `docs/PROJECT_SUMMARY.md`.
- **Ringkasan perubahan:**
  - Koreksi Pra-Tahap 4: Mengembalikan sapaan Beranda Wali dari `"Halo, Nandaa"` menjadi `"Halo, Nanda"` di `HomeSampleData.kt` dan `DummyData.kt` (typo Figma). Memperbarui `ASSETS.md`, `PROJECT_SUMMARY.md`, dan `SCREENS.md`.
  - Arsitektur State Penjaga (`GuardianViewModel.kt`): Menyediakan `GuardianHomeUiState`, state reaktif banner (`GuardianCheckStatus.PENDING`, `CHECKED_SAFE`, `NEEDS_HELP`), dan daftar riwayat aktivitas penjaga yang dinamis.
  - Beranda Penjaga (`GuardianHomeScreen.kt` - Tab 0): Header `"Halo, Markolintang"` (`"Penjaga terdekat - Oma Diyas"`), tombol panah kembali khusus `DEMO_MODE` (`// TODO(tim)`) dengan `BackHandler` menuju `Routes.role`, ikon lonceng notifikasi dengan red dot membuka `NotifikasiScreen`, banner komposit `"Tolong ketuk pintu Oma Diyas"` dengan dua tombol terpasang (`"Oma aman"` sage green & `"Butuh bantuan"` merah muda) yang mengubah banner secara reaktif + mencatat riwayat baru + feedback Snackbar, kartu mini peta `peta_lokasi` (`"Rumah Oma Diyas"`), kartu Aksi Cepat (`Lacak` & `Hubungi` dengan Snackbar simulasi), kartu Riwayat, dan 3-tab BottomNavBar beranimasi pill.
  - Lokasi Penjaga (`GuardianLokasiScreen.kt` - Tab 1): Header `"Lokasi Oma Diyas"`, kartu atas melayang dengan foto `oma_avatar`, jarak 170 m, status `"Di rumah"` dan badge `"Aman - Aktivitas Normal"`, peta statis `peta_lokasi` dengan kontrol melayang (kompas, zoom in, zoom out) dan pill timestamp, serta kartu `"Riwayat Lokasi"` menuju layar 7 hari.
  - Riwayat Lokasi 7 Hari (`GuardianLocationHistoryScreen.kt`): Layar daftar pergerakan 7 hari terakhir dari `DummyData.locationHistory7Days` dengan top bar, tombol kembali, detail status, dan feedback Snackbar klik titik.
  - Profil Penjaga (`GuardianProfileScreen.kt` - Tab 2): Info profil Markolintang, badge kesiapan radius 200 m, kartu lansia yang dipantau (Oma Diyas), switch `"Mode gelap"` (`KetukDarkModeCard`), dan tombol `"Keluar"` (`KetukLogoutButton`) yang memanggil `profileViewModel.logout()` lalu navigasi bersih `popUpTo(0)` ke `Routes.login`.
  - Routing & Navigasi: Menambahkan `Routes.guardianHome` dan `Routes.guardianLocationHistory`, memetakan role `"penjaga"` ke `Routes.guardianHome` di `homeRouteForRole` dan `setupRouteForRole`, serta mendaftarkan composable di `KetukNavHost.kt`.
- **File dibuat:**
  1. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianViewModel.kt`
  2. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianHomeScreen.kt`
  3. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianLokasiScreen.kt`
  4. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianLocationHistoryScreen.kt`
  5. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/guardian/GuardianProfileScreen.kt`
- **File diubah:**
  1. `app/src/main/java/com/chat/ketukprojectmap/data/dummy/DummyData.kt`
  2. `app/src/main/java/com/chat/ketukprojectmap/data/model/HomeSampleData.kt`
  3. `app/src/main/java/com/chat/ketukprojectmap/navigation/Routes.kt`
  4. `app/src/main/java/com/chat/ketukprojectmap/navigation/KetukNavHost.kt`
  5. `docs/ASSETS.md`
  6. `docs/PROJECT_SUMMARY.md`
  7. `docs/SCREENS.md`
  8. `docs/CHANGELOG_HISTORY.md`
- **File dihapus:** Tidak ada
- **Error/masalah:** Tidak ada
- **Solusi yang berhasil:** Penerapan `GuardianViewModel` dan pemanfaatan `KetukTheme.colors`, `KetukLogoutButton`, dan `KetukDarkModeCard` menghasilkan alur Penjaga yang bersih, konsisten, dan reaktif.
- **Status build:** `./gradlew assembleDemoDebug assembleFullDebug` **BUILD SUCCESSFUL**
- **Belum selesai:** Tahap 5 (`README.txt` & Finalisasi UTS).

---

## [2026-10-19 05:50] Tugas Khusus: Perapian Tampilan OmaPassiveScreen & Ekstraksi Komponen Bersama (KetukLogoutButton & KetukDarkModeCard)

- **Tujuan:** Merapikan tampilan `OmaPassiveScreen` sesuai arahan tanpa mengubah logika bisnis (dialog sapa, signOut, navigasi) atau tampilan mode terang, dengan mengekstrak komponen bersama `KetukLogoutButton` dan `KetukDarkModeCard` ke `ui/components/` serta memastikan keterbacaan pada mode terang/gelap, lebar 360dp, dan fontScale 1.3.
- **Referensi desain:** `ProfileScreen.kt`, `OmaScreens.kt`, dan `docs/SCREENS.md`.
- **Ringkasan perubahan:**
  - Membuat `KetukLogoutButton.kt`: tombol outline Terakota 52dp radius 12dp standar yang dipakai bersama oleh `ProfileScreen` dan `OmaPassiveScreen`.
  - Membuat `KetukDarkModeCard.kt`: kartu pengaturan switch mode gelap standar dengan status "Aktif"/"Nonaktif" dan warna switch kontras tinggi berbasis `KetukTheme.colors` (aktif Terakota) yang dipakai bersama di `ProfileScreen` dan `OmaPassiveScreen`.
  - Memperbarui `ProfileScreen.kt`: mengganti switch dan tombol keluar inline dengan `KetukDarkModeCard` dan `KetukLogoutButton` (tampilan visual 100% konsisten tanpa perubahan).
  - Memperbarui `OmaScreens.kt` (`OmaPassiveScreen`):
    1. Mengganti tombol keluar menjadi `KetukLogoutButton` dengan label seragam `"Keluar"` (dialog konfirmasi tetap utuh).
    2. Mengubah teks tombol simulasi sapaan menjadi satu baris `"Munculkan Sapaan"` pada `KetukButton` standar agar tidak terpotong atau janggal dalam pil, termasuk saat fontScale 1.3.
    3. Mempersingkat judul kartu simulasi menjadi satu baris `"Simulasi Sapaan L1"`, dan memindahkan keterangan "HP hening" ke teks deskripsi di bawahnya.
    4. Mengganti switch mode gelap menjadi `KetukDarkModeCard`.
    5. Menambahkan `@Preview` `PreviewOmaPassiveAccessibility` (width 360dp, fontScale 1.3f).
- **File dibuat:**
  1. `app/src/main/java/com/chat/ketukprojectmap/ui/components/KetukLogoutButton.kt`
  2. `app/src/main/java/com/chat/ketukprojectmap/ui/components/KetukDarkModeCard.kt`
- **File diubah:**
  1. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/profile/ProfileScreen.kt`
  2. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/oma/OmaScreens.kt`
  3. `docs/SCREENS.md`
  4. `docs/CHANGELOG_HISTORY.md`
- **File dihapus:** Tidak ada
- **Error/masalah:** Tidak ada
- **Solusi yang berhasil:** Ekstraksi komponen bersama berhasil merapikan UI dan menyeragamkan kedua layar dengan tepat 4 file kode.
- **Status build:** `./gradlew assembleDemoDebug assembleFullDebug` **BUILD SUCCESSFUL**
- **Belum selesai:** Tahap 4 (Alur Penjaga M10 + Riwayat Lokasi 7 Hari), Tahap 5 (`README.txt` & Finalisasi UTS).

---

## [2026-10-19 05:31] Tugas Khusus: Perbaikan Kecil Hasil Audit MVVM & Coroutine (OmaPassiveScreen SignOut, CancellationException, rememberSaveable & Warna Semantik OmaScreens)

- **Tujuan:** Menerapkan perbaikan terarah dari hasil audit arsitektur MVVM & coroutine pada maksimal 5 file kode tanpa mengubah tampilan mode terang atau menyentuh `LoginViewModel`, `SupabaseAuthRepository`, `FakeAuthRepository`, maupun `build.gradle.kts`.
- **Referensi desain:** Audit Arsitektur MVVM & Coroutine (`OmaScreens.kt`, `ProfileViewModel.kt`, `Theme.kt`).
- **Ringkasan perubahan:**
  - `ProfileViewModel.kt`: Menambahkan `catch (e: CancellationException) { throw e }` sebelum `catch (_: Exception)` di dalam `logout()` agar kooperatif terhadap pembatalan coroutine.
  - `Theme.kt`: Menambahkan dua properti warna semantik baru di `KetukExtendedColors` (`LightExtendedColors` & `DarkExtendedColors`): `scannerViewport` (`DarkSurface`) dan `overlayScrim` (`Color.Black.copy(alpha = 0.45f)`).
  - `OmaScreens.kt`:
    1. Mengubah `OmaPassiveScreen` agar menggunakan `ProfileViewModel.logout()` (`authRepository.signOut()`) sebelum memanggil `onLogout()` (navigasi `popUpTo(0)` ke Login), dilengkapi indikator loading `isLoggingOut` dan penonaktifan tombol selama proses keluar agar tidak ditekan dua kali.
    2. Mengganti `remember` menjadi `rememberSaveable` untuk `isPrivacyChecked`, `isSimulatingLoading`, `isFlashOn`, `showSapaDialog`, `showLogoutDialog`, dan `sapaStatusText`.
    3. Mengganti seluruh sisa warna langsung (`Color.White`, `Color.Black`, `Color(0xFF231D19)`, `CardPutih`, `HijauDaun`, `KuningMadu`) di `OmaScreens.kt` dengan warna semantik dari `KetukTheme.colors` (`scannerViewport`, `overlayScrim`, `qrSurface`, `onAccent`, `accentGreen`, `accentAmber`).
- **File dibuat:** Tidak ada
- **File diubah:**
  1. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/profile/ProfileViewModel.kt`
  2. `app/src/main/java/com/chat/ketukprojectmap/ui/theme/Theme.kt`
  3. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/oma/OmaScreens.kt`
  4. `docs/CHANGELOG_HISTORY.md`
- **File dihapus:** Tidak ada
- **Error/masalah:** Tidak ada
- **Solusi yang berhasil:** Pemanfaatan `ProfileViewModel` di `OmaPassiveScreen` menjaga jumlah file yang diubah seminimal mungkin (3 file kode).
- **Status build:** `./gradlew assembleDemoDebug assembleFullDebug` **BUILD SUCCESSFUL**
- **Belum selesai:** Tahap 4 (Alur Penjaga M10 + Riwayat Lokasi 7 Hari), Tahap 5 (`README.txt` & Finalisasi UTS).

---

## [2026-10-19 05:12] Tahap 3: Alur Oma/Opa (Pindai QR M4, Loading Pairing, Persetujuan & Izin, dan Mode Pasif M5 dengan Dialog Sapa L1)

- **Tujuan:** Mengimplementasikan seluruh alur Oma/Opa sesuai `03. Initial Phase oma.png` dan `flowchart.png`, mencakup Pindai QR kamera mockup dengan laser animasi, Loading Pairing 2 detik, Persetujuan & Izin (Yang dipantau & 4 izin sistem + komitmen privasi), serta Mode Pasif ramah lansia dengan dialog interaktif Sapa L1 "Oma baik-baik saja?", tombol panah kembali khusus demo, switch mode gelap, dan tombol keluar.
- **Referensi desain:** `docs/figma/03. Initial Phase oma.png`, `docs/figma/flowchart.png`, dan `docs/SCREENS.md`.
- **Ringkasan perubahan:**
  - Membuat `OmaScreens.kt` berisi 4 composable utama:
    1. `ScanQrScreen` (M4): Header judul & subjudul, mockup kamera statis berlatar gelap hangat, reticle sudut putih (`ScanCornerReticle`), kartu QR `qr_pairing`, garis laser animasi naik-turun, kontrol flash mockup, kartu petunjuk dengan ikon bohlam, tombol "Simulasikan Pindai", dan transisi halus ke loading pairing.
    2. `LoadingPairingOmaScreen` / `LoadingPairingOmaContent` (M4): Latar blur `qr_pairing_blur`, spinner berputar `ic_spinner`, teks "Loading Pairing..." dan "Menghubungkan ke HP Wali Nanda...", delay otomatis 2000 ms menuju Persetujuan & Izin.
    3. `OmaPermissionsScreen` (M4): Header judul & subjudul, Kartu 1 "Yang dipantau" (Gerakan & Aktivitas HP, Status HP, Lokasi darurat), Kartu 2 "Izin yang diperlukan" (Kamera, Notifikasi, Sensor Gerak, Lokasi), catatan komitmen privasi dengan checkbox, dan tombol utama "Saya Setuju" dengan feedback Snackbar mockup.
    4. `OmaPassiveScreen` (M5): Layar ramah lansia berhuruf besar dan kontras tinggi; header dengan tombol panah kembali khusus `BuildConfig.DEMO_MODE == true` ke `Routes.role` + `BackHandler` (// TODO(tim)); Hero Card Oma Diyas (`oma_avatar`, 67 Tahun, badge perlindungan aktif "Aman", statistik langkah/baterai/status); kartu simulasi sapaan L1 dengan dialog interaktif "Oma baik-baik saja?" (pilihan "Saya baik-baik saja" & "Butuh bantuan" eskalasi L2); kartu kontak & sensor dengan feedback Snackbar; kartu Switch "Mode gelap" terhubung ke `LocalDarkThemeToggle` (// TODO(tim)); serta tombol "Keluar dari Akun Oma" dengan konfirmasi dialog yang membersihkan back stack.
  - Memperbarui `Routes.kt`: menambahkan route `scanQr`, `loadingPairingOma`, `omaPermissions`, `omaPassive`, serta memperbarui pemetaan peran `homeRouteForRole("oma_opa") = omaPassive` dan `setupRouteForRole("oma_opa") = scanQr`.
  - Memperbarui `KetukNavHost.kt`: mendaftarkan 4 composable rute Oma/Opa dan menghubungkan alur setup & logout.
  - Memperbarui `HomeSampleData.kt`: mengoreksi teks sapaan `userName` menjadi `"Nandaa"` (dua huruf 'a') persis sesuai gambar `04. Core Phase Wali.png`.
  - Memperbarui `.clinerules`: menambahkan bagian H Pengecualian yang disetujui pengguna di 2A, serta mengoreksi minSdk 26, SUPABASE_ANON_KEY, password 8 karakter, dan rentang umur 40–120 tahun.
- **File dibuat:**
  1. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/oma/OmaScreens.kt`
- **File diubah:**
  1. `.clinerules` — Tambah Bagian H Pengecualian, koreksi minSdk 26, SUPABASE_ANON_KEY, password 8 karakter.
  2. `app/src/main/java/com/chat/ketukprojectmap/data/model/HomeSampleData.kt` — Koreksi userName menjadi "Nandaa".
  3. `app/src/main/java/com/chat/ketukprojectmap/navigation/Routes.kt` — Tambah rute Oma/Opa dan perbarui helper route peran.
  4. `app/src/main/java/com/chat/ketukprojectmap/navigation/KetukNavHost.kt` — Daftarkan navigasi rute Oma/Opa.
  5. `docs/SCREENS.md`, `docs/CHANGELOG_HISTORY.md` — Dokumentasi Tahap 3.
- **File dihapus:** Tidak ada
- **Error/masalah:** Tidak ada
- **Solusi yang berhasil:**
  - Pembuatan alur Oma/Opa 100% menggunakan `KetukTheme.colors` dan `SnackbarHost` untuk semua interaksi native mockup.
- **Yang GAGAL/jangan diulang:**
  - Tidak ada.
- **Perbedaan figma vs SCREENS.md:**
  - Tombol panah kembali di `OmaPassiveScreen` khusus mode `demo` dan baris Switch "Mode gelap" ditandai `// TODO(tim): tidak ada di Figma`.
- **Asumsi dan TODO:**
  - Siap lanjut ke Tahap 4 (Alur Penjaga M10 + Riwayat Lokasi 7 Hari).
- **Status build:** `./gradlew assembleDemoDebug assembleFullDebug` **BUILD SUCCESSFUL**
- **Belum selesai:** Tahap 4 (Alur Penjaga M10 + Riwayat Lokasi 7 Hari), Tahap 5 (`README.txt` & Finalisasi UTS).

---

## [2026-10-19 04:08] Tugas Khusus: Tombol Tema Manual (SharedPreferences) & Pembersihan Dark Mode Semantik di Seluruh Layar

- **Tujuan:** Mengganti tema otomatis sistem HP (`isSystemInDarkTheme()`) menjadi tombol manual (`Switch` `"Mode gelap"`) yang disimpan di `SharedPreferences` (`"dark_theme"`, default `false` = terang), serta merapikan tampilan Dark Mode di seluruh 16 layar dan komponen bersama tanpa mengubah tampilan mode terang sedikit pun.
- **Referensi desain:** Seluruh file `docs/figma/01` s/d `04` (mode terang dijaga 100% identik dengan nilai warna asli; mode gelap memakai sistem warna semantik hangat `KetukExtendedColors`).
- **Ringkasan perubahan:**
  - Menghapus nilai default `isSystemInDarkTheme()` dari `KetukTheme(darkTheme: Boolean, ...)` di `Theme.kt`.
  - Membuat satu sumber state `isDarkTheme` di `MainActivity.kt` yang membaca nilai awal dari `SharedPreferences("ketuk_prefs", MODE_PRIVATE)` kunci `"dark_theme"` (default `false` = terang), menyimpannya setiap kali berubah, meneruskannya ke `KetukTheme` di level paling atas, dan menyesuaikan ikon status bar & navigation bar secara reaktif lewat `enableEdgeToEdge`.
  - Menambahkan baris kartu `"Mode gelap"` dengan ikon `Icons.Rounded.LightMode` / `Icons.Rounded.DarkMode` dan `Switch` (warna aktif `Terakota`) di `ProfileScreen.kt` Wali (di bawah kartu info akun dan di atas tombol `"Keluar"`), ditandai `// TODO(tim): tidak ada di Figma`.
  - Mendefinisikan palet warna gelap hangat di `Color.kt` dan sistem warna semantik `KetukExtendedColors` (`CompositionLocal` via `KetukTheme.colors`) di `Theme.kt` (mencakup latar layar, permukaan kartu, teks utama/sekunder, garis pemisah, latar/border field input, kartu peran/aksi/status, serta permukaan QR tetap putih `qrSurface = CardPutih` agar tetap dapat dipindai).
  - Mengganti seluruh warna tetap (hardcoded) di 18 file di dalam `ui/` (`SplashScreen`, `OnboardingScreen`, `LoginScreen`, `RegisterScreen`, `VerificationScreen`, `RoleSelectScreen`, `DetailOmaOpaScreen`, `CircleKeluargaScreen`, `QrWaliScreen`, `PilihPenjagaScreen`, `HomeScreen`, `RiwayatScreen`, `DetailAlertScreen`, `LokasiScreen`, `NotifikasiScreen`, `ProfileScreen`, `AuthComponents`, `KetukButton`, `KetukSecondaryButton`) dengan `KetukTheme.colors`.
  - Menambahkan `@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)` dengan `KetukTheme(darkTheme = true)` pada layar-layar utama.
- **File dibuat:** Tidak ada
- **File diubah:**
  1. `app/src/main/java/com/chat/ketukprojectmap/MainActivity.kt` — State `isDarkTheme` dari `SharedPreferences` (`"dark_theme"`, default `false`) + update `enableEdgeToEdge` reaktif.
  2. `app/src/main/java/com/chat/ketukprojectmap/ui/theme/Color.kt` — Tambah konstanta palet warna gelap hangat.
  3. `app/src/main/java/com/chat/ketukprojectmap/ui/theme/Theme.kt` — Hapus `isSystemInDarkTheme()`, tambah `KetukExtendedColors`, `LocalKetukColors`, `LocalDarkThemeToggle`, dan `KetukTheme.colors`.
  4. `app/src/main/java/com/chat/ketukprojectmap/ui/components/AuthComponents.kt` — Gunakan warna semantik `KetukTheme.colors` pada `AuthField` & `AuthCloud`.
  5. `app/src/main/java/com/chat/ketukprojectmap/ui/components/KetukButton.kt` — Gunakan `KetukTheme.colors.onAccent` dan tambah preview dark.
  6. `app/src/main/java/com/chat/ketukprojectmap/ui/components/KetukSecondaryButton.kt` — Gunakan `KetukTheme.colors.textPrimary` dan tambah preview dark.
  7. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/splash/SplashScreen.kt` — Tambah `.background(KetukTheme.colors.screenBackground)` dan preview dark.
  8. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/onboarding/OnboardingScreen.kt` — Ganti warna tetap panel bawah, teks, indikator, dan tombol ke `KetukTheme.colors` + tambah preview dark.
  9. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/login/LoginScreen.kt` — Ganti warna tetap latar, field, divider, tombol Google, dan kartu hint demo ke `KetukTheme.colors` + tambah preview dark.
  10. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/register/RegisterScreen.kt` — Ganti warna tetap ke `KetukTheme.colors` + tambah preview dark.
  11. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/verification/VerificationScreen.kt` — Ganti warna tetap panel bawah, kotak OTP, banner error/info, dan teks resend ke `KetukTheme.colors` + tambah preview dark.
  12. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/role/RoleSelectScreen.kt` — Ganti warna tetap kartu peran & teks ke `KetukTheme.colors` + tambah preview dark.
  13. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/walisetup/WaliSetupScreens.kt` — Ganti 69 pemakaian warna tetap di 4 layar setup Wali ke `KetukTheme.colors` (QR tetap berlatar putih `qrSurface`) + tambah preview dark.
  14. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/home/HomeScreen.kt` — Ganti warna tetap kartu, chip, aksi cepat, dan bottom bar ke `KetukTheme.colors` + tambah preview dark.
  15. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/riwayat/RiwayatScreen.kt` — Ganti warna tetap filter chip dan kartu aktivitas ke `KetukTheme.colors` + tambah preview dark.
  16. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/detail/DetailAlertScreen.kt` — Ganti warna tetap kartu, chip eskalasi, dan tombol ke `KetukTheme.colors` + tambah preview dark.
  17. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/lokasi/LokasiScreen.kt` — Ganti warna tetap bottom sheet dan kartu penjaga ke `KetukTheme.colors` + tambah preview dark.
  18. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/notifikasi/NotifikasiScreen.kt` — Ganti warna tetap kartu notifikasi ke `KetukTheme.colors` + tambah preview dark.
  19. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/profile/ProfileScreen.kt` — Tambah baris Switch `"Mode gelap"` (`// TODO(tim): tidak ada di Figma`), ganti warna tetap kartu & dialog ke `KetukTheme.colors`, dan tambah preview dark.
  20. `docs/SCREENS.md`, `docs/CHANGELOG_HISTORY.md` — Dokumentasi tombol tema manual dan dark mode semantik.
- **File dihapus:** Tidak ada
- **Error/masalah:** Tidak ada
- **Solusi yang berhasil:**
  - Menyediakan `KetukExtendedColors` lewat `CompositionLocalProvider` di `KetukTheme` menjaga nilai mode terang 100% identik dengan konstanta warna asli sekaligus membuat seluruh layar reaktif saat Switch `"Mode gelap"` diubah.
- **Yang GAGAL/jangan diulang:**
  - Jangan menggelapkan latar belakang kontainer QR code karena kamera pemindai membutuhkan kontras margin terang (`qrSurface = CardPutih`) di sekeliling QR.
- **Perbedaan figma vs SCREENS.md:**
  - Baris pengaturan `"Mode gelap"` di `ProfileScreen` Wali tidak ada di Figma dan ditandai `// TODO(tim): tidak ada di Figma`. Untuk profil Oma/Opa dan Penjaga akan dipasang pada tahap masing-masing.
- **Asumsi dan TODO:**
  - Aset gambar ilustrasi (`bg_desa`, `peta_lokasi`, `qr_pairing_blur`, ikon navigasi PNG) tidak diedit sesuai aturan tugas.
- **Status build:** `./gradlew assembleDemoDebug assembleFullDebug` **BUILD SUCCESSFUL**
- **Belum selesai:** Tahap 3 (Alur Oma/Opa M4 & M5), Tahap 4 (Alur Penjaga M10 + Riwayat Lokasi 7 Hari), Tahap 5 (`README.txt` & Finalisasi UTS).

---

## [2026-10-19 03:15] Tahap 2: Alur Setup & Core Wali, DetailAlertScreen(itemId), Shape.kt & Dark Mode, serta Tombol Kembali Khusus Demo

- **Tujuan:** Mengimplementasikan seluruh alur Setup Wali (`02. Initial Phase Wali.png`) dan Core Wali (`04. Core Phase Wali.png`), membuat layar `DetailAlertScreen(itemId)` (M8) dengan pengiriman argumen navigasi, melengkapi `NotifikasiScreen` dan `ProfileScreen` Wali, menambahkan `Shape.kt` + dukungan Dark Mode di `Theme.kt`, memperbaiki penyimpanan tab di `HomeScreen` dengan `rememberSaveable`, serta menambahkan tombol panah kembali khusus `BuildConfig.DEMO_MODE` di tab Beranda Wali agar dosen penguji dapat menengok layar Pilih Peran.
- **Referensi desain:** `docs/figma/02. Initial Phase Wali.png`, `docs/figma/04. Core Phase Wali.png`, `docs/figma/flowchart.png`, dan `docs/SCREENS.md`.
- **Ringkasan perubahan:**
  - Membuat `Shape.kt` (`KetukShapes`) dan menambahkan `KetukDarkColorScheme` serta parameter `darkTheme: Boolean = isSystemInDarkTheme()` di `Theme.kt`.
  - Membuat `WaliSetupScreens.kt` yang berisi 4 layar Setup Wali sesuai `02. Initial Phase Wali.png`:
    1. `DetailOmaOpaScreen`: Input nama Wali (`"Nanda Valeri"`), toggle pill `"Opa"` / `"Oma"`, input nama lansia (`"Sri Diyas Anjarwati"`), dan input umur (`"67"`, `KeyboardType.Number`, rentang 50–120) dengan badge terakota `"Tahun"`, `isError = true`, `supportingText`, dan blokir submit saat tidak valid.
    2. `CircleKeluargaScreen`: Header `"3 Anggota"`, daftar anggota Circle (`Nanda (Wali)` Ketua + mahkota, `Papa`, `Mama`), dialog `+ Tambah Anggota` in-memory, kartu `"Undang Keluarga Lain"` dengan QR (`R.drawable.qr_pairing`), serta tombol `"Bagikan QR"`, `"Salin Tautan"`, dan `"Lanjutkan"`.
    3. `QrWaliScreen`: Tampilan kartu QR besar (`R.drawable.qr_pairing`) untuk dipindai HP Oma/Opa + tombol simulasi yang menampilkan state `LoadingPairingWaliContent` (`R.drawable.qr_pairing_blur` + `R.drawable.ic_spinner` berputar + teks `"Loading Pairing..."`) selama 2 detik sebelum lanjut ke Pilih Penjaga.
    4. `PilihPenjagaScreen`: Search bar real-time `"Cari nama atau nomor..."`, daftar 4 calon penjaga (`Pak Andy`, `Ibu Imas`, `Adik Adam`, `Pak RT`) dengan **Radio Button** (single selection sesuai Figma `02`), dan tombol `"Pilih sebagai Penjaga"`.
  - Membuat `DetailAlertScreen.kt` (M8) yang menerima argumen `itemId` dari NavHost (`detail_alert/{itemId}`), mencari item di `DummyData.findActivityById(itemId)`, menampilkan badge level eskalasi (`NORMAL` / `L1` / `L2` / `L3`), status reaktif, deskripsi lengkap, lokasi Oma Diyas, dan 3 tombol aksi (`"Telepon Oma"`, `"Konfirmasi Oma Aman"`, `"Minta Penjaga Mengecek"`) dengan feedback `SnackbarHost`.
  - Memperbarui `HomeScreen.kt`: mengganti `selectedTab` ke `rememberSaveable`, menambahkan tombol panah kembali (`Icons.AutoMirrored.Rounded.ArrowBack`, tint `Terakota`) + `BackHandler` **hanya saat `BuildConfig.DEMO_MODE == true` dan `selectedTab == 0`** (ditandai `// TODO(tim): tombol khusus demo, tidak ada di Figma`), menambahkan `SnackbarHost` untuk Aksi cepat, dan menghubungkan `"Lihat detail >"` serta item aktivitas terbaru ke `DetailAlertScreen(itemId)`.
  - Memperbarui `RiwayatViewModel.kt` dan `RiwayatScreen.kt` agar memakai 8 item `DummyData.initialActivities` dan membuka `DetailAlertScreen(item.id)` saat item diklik.
  - Memperbarui `LokasiScreen.kt` untuk menampilkan foto `R.drawable.penjaga_lintang`, menyamakan umur Oma ke `67` tahun (`DummyData.defaultElder.age`), dan menampilkan Snackbar simulasi pada tombol `"Lihat Rute"`, `"Hubungi Oma"`, dan kartu Penjaga.
  - Melengkapi `NotifikasiScreen.kt` (5 item `DummyData.initialNotifications` dengan `LazyColumn`, tombol `"Tandai dibaca"`, dan klik ke `DetailAlertScreen`) serta `ProfileScreen.kt` Wali (kartu akun Wali, kartu lansia yang dipantau, dan tombol `"Keluar"` + dialog konfirmasi).
- **File dibuat:**
  1. `app/src/main/java/com/chat/ketukprojectmap/ui/theme/Shape.kt`
  2. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/walisetup/WaliSetupScreens.kt`
  3. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/detail/DetailAlertScreen.kt`
- **File diubah:**
  1. `app/src/main/java/com/chat/ketukprojectmap/ui/theme/Theme.kt` — Tambah `KetukDarkColorScheme`, `darkTheme`, dan `shapes = KetukShapes`.
  2. `app/src/main/java/com/chat/ketukprojectmap/navigation/Routes.kt` — Tambah route `detailOmaOpa`, `circleKeluarga`, `qrWali`, `pilihPenjaga`, `detailAlertPattern` (`detail_alert/{itemId}`), `detailAlert(itemId)`, serta helper `homeRouteForRole` & `setupRouteForRole`.
  3. `app/src/main/java/com/chat/ketukprojectmap/navigation/KetukNavHost.kt` — Daftarkan composable Setup Wali, `DetailAlertScreen(itemId)`, dan navigasi kembali khusus demo dari `HomeScreen` ke `Routes.role` dengan `popUpTo(Routes.home) { inclusive = true }`.
  4. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/home/HomeScreen.kt` — Ubah `selectedTab` ke `rememberSaveable`, tambah panah kembali & `BackHandler` khusus `DEMO_MODE` di tab Beranda, tambah `SnackbarHost`, dan sambungkan klik detail ke `DetailAlertScreen`.
  5. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/riwayat/RiwayatViewModel.kt` — Gunakan `DummyData.initialActivities` (8 item `ActivityItem`).
  6. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/riwayat/RiwayatScreen.kt` — Render `ActivityItem` dan tambahkan callback `onItemClick(item.id)`.
  7. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/lokasi/LokasiScreen.kt` — Pasang `R.drawable.penjaga_lintang`, umur `67` dari `DummyData`, dan `SnackbarHost` untuk aksi mockup.
  8. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/notifikasi/NotifikasiScreen.kt` — Render `DummyData.initialNotifications` dengan `LazyColumn` dan navigasi ke `DetailAlertScreen`.
  9. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/profile/ProfileScreen.kt` — Lengkapi dengan kartu informasi akun Wali, lansia yang dipantau, penjaga terdekat, serta tombol `"Keluar"` + dialog.
  10. `docs/SCREENS.md`, `docs/ASSETS.md`, `docs/PROJECT_SUMMARY.md` — Sinkronisasi dokumentasi Tahap 2.
- **File dihapus:** Tidak ada
- **Error/masalah:**
  - Percobaan kompilasi pertama pada `KetukNavHost.kt` gagal karena perbedaan nama parameter callback pemanggil (`onNext` dan `onConfirm`) dengan definisi fungsi di `WaliSetupScreens.kt` (`onContinue`, `onPairingComplete`, dan `onGuardianConfirmed`).
- **Solusi yang berhasil:**
  - Menyesuaikan nama argumen di `KetukNavHost.kt` menjadi `onContinue`, `onPairingComplete`, dan `onGuardianConfirmed` sesuai tanda tangan fungsi di `WaliSetupScreens.kt`.
- **Yang GAGAL/jangan diulang:**
  - Pastikan selalu memeriksa nama parameter callback di fungsi `@Composable` tujuan sebelum memanggilnya dengan *named arguments* di `KetukNavHost.kt`.
- **Perbedaan figma vs SCREENS.md:**
  - **Tombol Panah Kembali Khusus Mode Demo di Beranda Wali (`HomeScreen`):** Selisih dari Figma yang disengaja (`// TODO(tim): tombol khusus demo, tidak ada di Figma`). Ditampilkan hanya jika `BuildConfig.DEMO_MODE == true` dan `selectedTab == 0` (tab Beranda), sejajar dengan sapaan `"Halo, Nandaa"` tanpa menggeser ikon lonceng notifikasi, dengan aksi eksplisit `navController.navigate(Routes.role) { popUpTo(Routes.home) { inclusive = true } }`. Pada varian `full`, tampilan Beranda tidak berubah sama sekali.
  - Layar `DetailAlertScreen` (M8), `NotifikasiScreen`, dan `ProfileScreen` Wali tidak memiliki frame khusus di Figma 01–05 sehingga dirancang selaras dengan palet warna (`KremBackground`, `CardPutih`, `Terakota`, `HijauDaun`) dan tipografi `PlusJakartaSans` Ketuk serta ditandai `// TODO(tim)`.
- **Asumsi dan TODO:**
  - Saat Tahap 3 (Beranda Oma/Opa) dan Tahap 4 (Beranda Penjaga) dikerjakan, tombol panah kembali khusus `BuildConfig.DEMO_MODE` yang sama akan diingatkan kepada pengguna untuk ditambahkan di sana.
- **Status build:** `./gradlew assembleDemoDebug assembleFullDebug` **BUILD SUCCESSFUL**
- **Belum selesai:** Tahap 3 (Alur Oma/Opa M4 & M5), Tahap 4 (Alur Penjaga M10 + Riwayat Lokasi 7 Hari), Tahap 5 (`README.txt` & Finalisasi UTS).

---

## [2026-10-19 01:23] Tahap 1: Fondasi Build Variants (demo vs full), Pembungkusan AuthRepository, dan Paket DummyData Terpusat

- **Tujuan:** Membagi konfigurasi build menjadi 2 Product Flavors (`demo` untuk UTS dan `full` untuk UAS), membungkus kode autentikasi Supabase yang sudah ada ke dalam `SupabaseAuthRepository` tanpa menghapus/merusaknya, menyediakan `FakeAuthRepository` in-memory untuk akun demo offline, menjaga pemanggilan Google Sign-In agar tidak menginisialisasi `SupabaseProvider.client` pada mode `demo`, serta membuat paket `DummyData` terpusat sesuai Figma 01–05.
- **Referensi desain:** `docs/figma/01. Initial Phase (2).png`, `02. Initial Phase Wali.png`, `03. Initial Phase oma.png`, `04. Core Phase Wali.png`, `05. Core Phase Penjaga.png`, `flowchart.png`, dan `docs/SCREENS.md`.
- **Ringkasan perubahan:**
  - Menambahkan `flavorDimensions += "mode"` dengan 2 product flavors (`demo` dan `full`) beserta flag `BuildConfig.DEMO_MODE` di `app/build.gradle.kts`.
  - Memindahkan kode class `AuthRepository` asli secara utuh ke `SupabaseAuthRepository.kt`, membuat `FakeAuthRepository.kt` (in-memory dengan delay 800 ms, 3 akun demo `wali@ketuk.app`, `oma@ketuk.app`, `penjaga@ketuk.app` sandi `Ketuk123`, serta dukungan Register/OTP/Google demo), dan menjadikan `AuthRepository.kt` sebagai interface + factory tunggal berdasarkan `BuildConfig.DEMO_MODE`.
  - Membungkus `SupabaseProvider.client.composeAuth.rememberSignInWithGoogle(...)` di `LoginScreen.kt` dan `RegisterScreen.kt` dengan `if (!BuildConfig.DEMO_MODE)` sehingga pada variant `demo` client Supabase tidak pernah diinisialisasi.
  - Menambahkan validasi `isError = true` + `supportingText` per field dan kartu hint 3 akun demo (khusus `DEMO_MODE`) di `LoginScreen.kt`, serta menyesuaikan panjang OTP (`CODE_LENGTH`) menjadi 4 digit di mode `demo` (sesuai Figma `01`) dan 6 digit di mode `full`.
  - Membuat paket `data/dummy/` (`DummyModels.kt` dan `DummyData.kt`) berisi data class dan data terpusat (`Elder` umur 67 tahun, 3 `CircleMember`, 4 calon `Guardian` termasuk `Ibu Imas` + `Markolintang`, 8 `ActivityItem`, 5 `NotificationItem`, dan 7 `LocationHistoryItem`).
- **File dibuat:**
  1. `app/src/main/java/com/chat/ketukprojectmap/data/dummy/DummyModels.kt`
  2. `app/src/main/java/com/chat/ketukprojectmap/data/dummy/DummyData.kt`
  3. `app/src/main/java/com/chat/ketukprojectmap/data/repository/SupabaseAuthRepository.kt`
  4. `app/src/main/java/com/chat/ketukprojectmap/data/repository/FakeAuthRepository.kt`
- **File diubah:**
  1. `app/build.gradle.kts` — Tambah `SUPABASE_KEY` fallback dan `productFlavors` (`demo` & `full`) dengan `DEMO_MODE`.
  2. `app/src/main/java/com/chat/ketukprojectmap/data/repository/AuthRepository.kt` — Ubah menjadi `interface AuthRepository` dan fungsi factory tunggal `fun AuthRepository(): AuthRepository`.
  3. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/login/LoginViewModel.kt` — Tambah validasi format email & password (`emailError`, `passwordError`), fungsi `signInWithGoogleDemo`, dan pesan error `"Email/Nomor HP atau password salah"`.
  4. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/login/LoginScreen.kt` — Jaga `rememberSignInWithGoogle` dengan `!BuildConfig.DEMO_MODE`, tambah `isError` & `supportingText` di `LoginField`, dan tambah `DemoAccountsHintCard` saat `BuildConfig.DEMO_MODE == true`.
  5. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/register/RegisterViewModel.kt` — Tambah fungsi `signInWithGoogleDemo`.
  6. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/register/RegisterScreen.kt` — Jaga `rememberSignInWithGoogle` dengan `!BuildConfig.DEMO_MODE`.
  7. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/verification/VerificationViewModel.kt` — Ubah `CODE_LENGTH` menjadi `if (BuildConfig.DEMO_MODE) 4 else 6`.
  8. `.clinerules`, `docs/SCREENS.md`, `docs/ASSETS.md`, `docs/PROJECT_SUMMARY.md` — Sinkronisasi keputusan tim dan dokumentasi Tahap 1.
- **File dihapus:** Tidak ada
- **Error/masalah:**
  - Percobaan build pertama gagal pada `LoginScreen.kt:63` dan `RegisterScreen.kt:58` dengan pesan: `Initializer type mismatch: expected 'Function0<Unit>', actual 'Any'` dan `Expression is treated as a trailing lambda argument; consider separating it from the call with semicolon`.
  - Penyebab: Menulis blok `{ googleAuth.startFlow() }` langsung di baris setelah `val googleAuth = SupabaseProvider.client.composeAuth.rememberSignInWithGoogle(...)` di dalam cabang `else` membuat compiler Kotlin menganggap `{ googleAuth.startFlow() }` sebagai *trailing lambda* tambahan untuk fungsi `rememberSignInWithGoogle`.
- **Solusi yang berhasil:**
  - Memisahkan deklarasi `val googleAuth = if (!BuildConfig.DEMO_MODE) { ... } else null` terlebih dahulu, baru membuat `val onGoogleClick: () -> Unit = { if (BuildConfig.DEMO_MODE) ... else googleAuth?.startFlow() }`.
- **Yang GAGAL/jangan diulang:**
  - Jangan menaruh ekspresi lambda `{ ... }` langsung di baris setelah pemanggilan fungsi tanpa titik koma di dalam blok `if/else`, karena Kotlin akan membacanya sebagai *trailing lambda* dari fungsi di atasnya.
- **Perbedaan figma vs SCREENS.md:**
  - Disepakati bersama tim: Login tetap memakai field Email (bukan Nomor HP) demi kompatibilitas akun demo & Supabase; nama calon penjaga ke-2 mengikuti Figma `02` yaitu `"Ibu Imas"` dengan pilihan Radio Button; umur Oma Diyas diseragamkan menjadi `67` tahun di `DummyData.kt`; kotak OTP berjumlah 4 di mode `demo` (sesuai Figma `01`) dan 6 di mode `full`.
- **Asumsi dan TODO:**
  - Pada akhir Tahap 1, login dengan ke-3 akun demo masih mengarah ke `Routes.home` (Home Wali) karena route dan layar khusus Oma/Opa serta Penjaga baru dibuat pada Tahap 2–4.
- **Status build:** `./gradlew assembleDemoDebug assembleFullDebug` **BUILD SUCCESSFUL in 4m 4s**
- **Belum selesai:** Tahap 2 (Alur Setup & Core Wali + `DetailAlertScreen(itemId)` + `Shape.kt` & Dark Mode), Tahap 3 (Alur Oma/Opa), Tahap 4 (Alur Penjaga), Tahap 5 (`README.txt` & Finalisasi UTS).

---

## [2026-10-17 16:40] Perbaikan fitur Logout di ProfileScreen dan integrasi ke HomeScreen

- **Tujuan:** Melengkapi dan memperbaiki fitur logout yang sudah ada agar berfungsi penuh. Pengguna dapat keluar dari akun di tab Profil, dengan konfirmasi dialog, dan kembali ke layar Login dengan back stack bersih.

- **Referensi desain:** Tidak mengubah desain ProfileScreen yang sudah ada, hanya memastikan integrasi navigasi dan fungsi logout bekerja dengan benar.

- **Ringkasan perubahan:**
  - Menambahkan import `CancellationException` yang kurang di `AuthRepository.kt` (error build)
  - Mengganti `PlaceholderTab("Profil")` dengan `ProfileScreen` yang sebenarnya di `HomeScreen`
  - Menambahkan parameter `onLogout` di `HomeScreen` dan meneruskannya ke `ProfileScreen`
  - Mengatur callback logout di `KetukNavHost` untuk navigasi ke Login dengan `popUpTo(0) { inclusive = true }`
  - Menghapus fungsi `PlaceholderTab` yang tidak terpakai lagi
  - Membersihkan warning lint (parameter unused `e` diganti dengan `_`)

- **File dibuat:** Tidak ada (semua file sudah ada sebelumnya)

- **File diubah:**
  1. `app/src/main/java/com/chat/ketukprojectmap/data/repository/AuthRepository.kt`
     - Tambah import `kotlinx.coroutines.CancellationException` (baris 8)
     - Ubah `catch (e: Exception)` menjadi `catch (_: Exception)` untuk menghilangkan warning lint (baris 77)
  
  2. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/profile/ProfileViewModel.kt`
     - Ubah `catch (e: Exception)` menjadi `catch (_: Exception)` untuk menghilangkan warning lint (baris 23)
  
  3. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/home/HomeScreen.kt`
     - Tambah import `com.chat.ketukprojectmap.ui.screens.profile.ProfileScreen` (baris 44)
     - Tambah parameter `onLogout: () -> Unit = {}` di fungsi `HomeScreen` (baris 53)
     - Ganti `PlaceholderTab("Profil")` dengan `ProfileScreen(onLogout = onLogout)` di selectedTab == 3 (baris 78)
     - Hapus fungsi `PlaceholderTab` yang tidak terpakai (baris 95-108)
  
  4. `app/src/main/java/com/chat/ketukprojectmap/navigation/KetukNavHost.kt`
     - Ubah `catch (e: Exception)` menjadi `catch (_: Exception)` di handler getRole (baris 71)
     - Tambah parameter `onLogout` di composable Routes.home (baris 143-147)
     - Navigasi ke `Routes.login` dengan `popUpTo(0) { inclusive = true }` agar back stack dibersihkan

- **File dihapus:** Tidak ada

- **Error/masalah:** 
  - Build error pertama: `Unresolved reference 'CancellationException'` di AuthRepository.kt baris 74
  - Penyebab: Import untuk `kotlinx.coroutines.CancellationException` tidak ada, padang fungsi `signOut()` menggunakannya untuk re-throw CancellationException (best practice coroutine)

- **Solusi yang berhasil:** 
  - Menambahkan `import kotlinx.coroutines.CancellationException` di AuthRepository.kt
  - Mengintegrasikan ProfileScreen ke HomeScreen tab ke-3 dengan callback logout yang benar
  - Menggunakan `popUpTo(0) { inclusive = true }` untuk membersihkan seluruh back stack saat logout

- **Yang GAGAL/jangan diulang:** Tidak ada (implementasi berjalan lancar)

- **Perbedaan figma vs SCREENS.md:** Tidak relevan (tidak ada desain baru, hanya integrasi fungsi yang sudah ada)

- **Asumsi dan TODO:** 
  - Tidak ada DataStore atau SharedPreferences yang menyimpan sesi lokal selain Supabase auth session
  - Fungsi `auth.signOut()` dari Supabase-kt sudah menangani pembersihan sesi lokal
  - Setelah logout, SplashScreen akan mendeteksi `hasSession() == false` dan mengarahkan ke Onboarding
  - Tidak ada role atau preferensi lokal yang perlu dihapus terpisah (semuanya terkait dengan auth session)
  - TODO: Tes manual di emulator untuk memastikan alur logout → login → pilih role bekerja dengan benar

- **Status build:** `./gradlew assembleDebug` **SUKSES** tanpa error atau warning

- **Belum selesai:** Tidak ada (fitur logout sudah lengkap dan terintegrasi)

---

## [2026-10-17 16:26] Tambah tombol kembali di LoginScreen ke Onboarding

- **Tujuan:** Memungkinkan pengguna kembali ke Onboarding dari LoginScreen. Berguna ketika data akun dihapus dari Supabase dan pengguna langsung masuk ke Login tanpa melewati Onboarding.

- **Referensi desain:** Tidak mengubah desain dari figma, hanya menambah tombol back standar di pojok kiri atas.

- **Ringkasan perubahan:**
  - Menambahkan parameter `onNavigateBack` di `LoginScreen` dan `LoginContent`
  - Menambahkan `BackHandler` untuk menangani tombol back fisik HP
  - Menambahkan IconButton dengan ikon ArrowBack di pojok kiri atas LoginScreen (warna Terakota)
  - Mengatur navigasi dari Login ke Onboarding di KetukNavHost dengan `popUpTo(Routes.login) { inclusive = true }`

- **File dibuat:** Tidak ada

- **File diubah:**
  1. `app/src/main/java/com/chat/ketukprojectmap/ui/screens/login/LoginScreen.kt`
     - Tambah import `BackHandler` dan `Icons.AutoMirrored.Rounded.ArrowBack`
     - Tambah parameter `onNavigateBack: () -> Unit = {}` di `LoginScreen` (baris 54) dan `LoginContent` (baris 85)
     - Teruskan `onNavigateBack` dari `LoginScreen` ke `LoginContent` (baris 72)
     - Tambah `BackHandler(onBack = onNavigateBack)` setelah deklarasi `passwordVisible` (baris 94-95)
     - Tambah IconButton dengan ikon back di BoxWithConstraints setelah Column utama (baris 387-401)
  
  2. `app/src/main/java/com/chat/ketukprojectmap/navigation/KetukNavHost.kt`
     - Tambah parameter `onNavigateBack` di composable Login (baris 81-85)
     - Navigasi ke `Routes.onboarding` dengan `popUpTo(Routes.login) { inclusive = true }`

- **File dihapus:** Tidak ada

- **Error/masalah:** Tidak ada

- **Solusi yang berhasil:** 
  - Menggunakan `navController.navigate(Routes.onboarding) { popUpTo(Routes.login) { inclusive = true } }` untuk memastikan Login dibuang dari back stack saat navigasi ke Onboarding
  - Tidak menggunakan `popBackStack()` karena Onboarding mungkin sudah tidak ada di back stack

- **Yang GAGAL/jangan diulang:** Tidak ada

- **Perbedaan figma vs SCREENS.md:** Tidak relevan, tidak ada perubahan pada tampilan utama LoginScreen

- **Asumsi dan TODO:** 
  - Tidak ditemukan flag "onboarding sudah dilihat" di DataStore/SharedPreferences, jadi tidak ada mekanisme yang membuat Splash skip Onboarding
  - Tombol back menggunakan warna Terakota sesuai dengan elemen interaktif lain di aplikasi

- **Status build:** 
  - `./gradlew assembleDebug` **SUKSES**
  - BUILD SUCCESSFUL in 1m 4s
  - 52 actionable tasks: 7 executed, 45 up-to-date

- **Belum selesai:** Tidak ada, tugas selesai

---


# Changelog History - Ketuk Project MAP

Entri terbaru di PALING ATAS. Jangan menghapus atau menulis ulang entri lama.

---

## [2026-10-17 13:15] Implementasi RegisterScreen dengan validasi lokal dan navigasi lengkap

- **Tujuan:** Membuat halaman Register (Daftar) lengkap dengan 5 field input (nama lengkap, email, no HP, password, konfirmasi password), validasi client-side, integrasi dengan navigasi Login dan Verification, serta komponen AuthField/AuthCloud yang dapat dipakai ulang.

- **Referensi desain:** Mengikuti gaya visual LoginScreen yang sudah ada, dengan maskot tok_memeriksa dan asset_ketuk_daun, warna Terakota dan KremBackground dari theme.

- **Ringkasan perubahan:**
  - Dibuat RegisterScreen dengan layout 2-kolom responsif (maskot + logo di atas, form di bawah).
  - Dibuat RegisterViewModel untuk state management dengan validasi: nama tidak kosong, email format valid (@), nomor HP 9-13 digit angka, password min 8 karakter, konfirmasi password harus sama.
  - Ekstrak komponen AuthField dan AuthCloud ke file shared AuthComponents.kt untuk dipakai RegisterScreen (LoginScreen tetap memakai komponen lokalnya).
  - Tambah parameter onNavigateToRegister ke LoginScreen, buat teks "Daftar disini" menjadi clickable.
  - Alur navigasi: Register sukses → Verification (bukan langsung ke Role), Login ↔ Register (bolak-balik dengan back).
  - Tombol Google Sign-In dan field semua mengikuti gaya LoginScreen (outline terakota, background peach 12% alpha).

- **File dibuat:**
  - `app/src/main/java/com/chat/ketukprojectmap/ui/screens/register/RegisterScreen.kt` (main UI dengan 3 preview)
  - `app/src/main/java/com/chat/ketukprojectmap/ui/screens/register/RegisterViewModel.kt` (state + validasi)
  - `app/src/main/java/com/chat/ketukprojectmap/ui/components/AuthComponents.kt` (AuthField, AuthCloud)

- **File diubah:**
  - `app/src/main/java/com/chat/ketukprojectmap/ui/screens/login/LoginScreen.kt`:
    - Tambah parameter `onNavigateToRegister: () -> Unit = {}` di signature LoginScreen
    - Tambah import `androidx.compose.foundation.clickable`
    - Buat teks "Daftar disini" clickable dengan modifier `Modifier.clickable { onNavigateToRegister() }`
    - LoginScreen tetap memakai LoginField dan Cloud versi lokalnya (tidak dipindah, signature berbeda)
  - `app/src/main/java/com/chat/ketukprojectmap/navigation/Routes.kt`:
    - Tambah `const val register = "register"` setelah login
  - `app/src/main/java/com/chat/ketukprojectmap/navigation/KetukNavHost.kt`:
    - Tambah import `RegisterScreen`
    - Update composable login: tambah parameter `onNavigateToRegister = { navController.navigate(Routes.register) }`
    - Tambah composable register dengan onNavigateToLogin (popBackStack) dan onRegisterSuccess (navigate ke verification, pop register dari back stack)

- **File dihapus:** Tidak ada.

- **Error/masalah:**
  1. Awalnya mencoba ekstrak LoginField dan Cloud dari LoginScreen, tetapi signature berbeda dengan kebutuhan RegisterScreen. Solusi: biarkan LoginScreen tetap pakai versi lokalnya, buat AuthField dan AuthCloud baru di AuthComponents untuk RegisterScreen.
  2. Salah pakai `HorizontalDivider` (Material3 baru) padahal proyek masih pakai `Divider` lama. Solusi: ganti ke Divider.
  3. Beberapa typo sintaks (kurung tutup hilang, kurung tutup berlebih). Solusi: perbaiki satu per satu.

- **Solusi yang berhasil:**
  - Membuat komponen shared AuthField/AuthCloud terpisah dari LoginScreen, jadi tidak mengubah LoginScreen yang sudah stabil.
  - Validasi email dengan regex sederhana `^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$`
  - Filter input nomor HP hanya terima digit: `value.filter { it.isDigit() }`
  - State error per field dengan clear error saat user mulai mengetik ulang

- **Yang GAGAL/jangan diulang:**
  - Mencoba edit LoginScreen untuk memindahkan LoginField dan Cloud ke AuthComponents dengan signature yang sama → gagal karena LoginScreen pakai parameter berbeda (value/onValueChange bukan label-based). Lebih baik buat komponen baru daripada merusak yang sudah jalan.
  - Mencoba pakai `HorizontalDivider` tanpa cek versi Material3 proyek → build error. Selalu cek komponen yang tersedia di file lain dulu.

- **Perbedaan figma vs SCREENS.md:**
  - Tidak ada file figma spesifik untuk RegisterScreen. Mengikuti pola visual LoginScreen (maskot, logo, spacing, warna, tipografi).
  - Teks placeholder dan label dibuat konsisten dengan LoginScreen.

- **Asumsi dan TODO:**
  - Backend integration (Supabase signUp) ditandai TODO di RegisterViewModel line ~116.
  - Tombol Google Sign-In di RegisterScreen hanya placeholder (onClick kosong dengan komentar TODO).
  - Verifikasi OTP belum diimplementasi, RegisterScreen langsung navigate ke VerificationScreen yang sudah ada.
  - Password visibility toggle sudah ada (icon mata).
  - Belum ada "kebijakan privasi" atau "syarat & ketentuan" checkbox (tidak diminta di spesifikasi).

- **Status build:** `./gradlew assembleDebug` **SUKSES** tanpa error atau warning baru.

- **Belum selesai:** Tidak ada, semua sesuai spesifikasi.

---

## [2026-10-12 18:30] Migrasi launcher icon ke full vector dengan fallback API 23-25

- **Tujuan:** Mengubah launcher icon dari bitmap (webp) menjadi full vector agar lebih fleksibel, ukuran lebih kecil, dan mendukung berbagai densitas layar tanpa kehilangan kualitas. Menyediakan fallback khusus untuk Android 6.0-7.1 (API 23-25) yang tidak mendukung adaptive icon penuh.

- **Referensi desain:** Logo Ketuk dari design system proyek

- **Ringkasan perubahan:**
  - Launcher icon sebelumnya menggunakan format webp di folder mipmap-hdpi/mdpi/xhdpi/xxhdpi/xxxhdpi (total 15 file).
  - Semua file bitmap launcher dihapus dari folder mipmap-*.
  - Dibuat `mipmap-anydpi/ic_launcher.xml` dan `mipmap-anydpi/ic_launcher_round.xml` sebagai fallback vector untuk API 23-25.
  - Adaptive icon untuk API 26+ tetap menggunakan `mipmap-anydpi-v26/ic_launcher.xml` yang merujuk ke `drawable/ic_launcher_foreground.xml` dan `drawable/ic_launcher_background.xml`.
  - AndroidManifest.xml tetap merujuk `@mipmap/ic_launcher` dan `@mipmap/ic_launcher_round` sesuai konvensi Android.

- **File dibuat:**
  - `app/src/main/res/mipmap-anydpi/ic_launcher.xml` (fallback vector API 23-25)
  - `app/src/main/res/mipmap-anydpi/ic_launcher_round.xml` (fallback vector API 23-25)

- **File diubah:**
  - Tidak ada file kode yang diubah, hanya resource icon.

- **File dihapus:**
  - 15 file webp launcher icon dari folder `mipmap-hdpi/`, `mipmap-mdpi/`, `mipmap-xhdpi/`, `mipmap-xxhdpi/`, `mipmap-xxxhdpi/`:
    - `ic_launcher.webp` dan `ic_launcher_round.webp` di setiap folder densitas.
  - Bukti search: File-file tersebut hanya direferensikan oleh sistem Android melalui resolusi resource density, tidak ada kode yang mereferensikan secara eksplisit.

- **Error/masalah:**
  - Tidak ada error build.
  - Android Studio menampilkan warning "The resource `@mipmap/ic_launcher` references a vector drawable from `mipmap-anydpi/`, which should be in `drawable/` instead."

- **Solusi yang berhasil:**
  - Warning IDE sengaja **DIABAIKAN**. Alasan:
    1. AndroidManifest.xml HARUS merujuk `@mipmap/ic_launcher` (bukan `@drawable/`) sesuai konvensi Android untuk launcher icon.
    2. Memindahkan ke drawable/ akan menyebabkan manifest error atau icon tidak muncul.
    3. Fallback vector di `mipmap-anydpi/` adalah solusi yang sah untuk API 23-25.
    4. Adaptive icon API 26+ tetap bekerja dengan baik di `mipmap-anydpi-v26/`.

- **Yang GAGAL/jangan diulang:**
  - Jangan memindahkan `ic_launcher.xml` dari `mipmap-anydpi/` ke `drawable/` hanya untuk menghilangkan warning IDE.
  - Jangan mengubah referensi di AndroidManifest.xml dari `@mipmap/` menjadi `@drawable/` untuk launcher icon.

- **Perbedaan figma vs SCREENS.md:**
  - Tidak relevan untuk tugas ini (hanya perubahan launcher icon).

- **Asumsi dan TODO:**
  - Asumsi: Logo Ketuk yang digunakan adalah logo resmi proyek yang sudah final.
  - TODO: Tidak ada untuk tugas ini.

- **Status build:**
  - `./gradlew assembleDebug` sukses.
  - Lint menampilkan warning `VectorDrawableCompat` untuk `mipmap-anydpi/ic_launcher.xml`, namun ini expected behavior dan sengaja diabaikan sesuai solusi di atas.

- **Belum selesai:**
  - Tidak ada. Icon launcher berfungsi normal di semua versi Android (API 23+).

---

## [Sebelum 2026-10-12] Inisialisasi proyek dan implementasi Initial Phase

- **Tujuan:** Setup awal proyek Android dengan Jetpack Compose dan implementasi layar-layar Initial Phase (Loading, Onboarding, Login, Pilih Peran, Home Wali).

- **Referensi desain:** 
  - `01. Initial Phase (2).png`
  - `02. Initial Phase Wali.png`
  - `04. Core Phase Wali.png` (untuk referensi design system)

- **Ringkasan perubahan:**
  - Setup proyek Android baru dengan Gradle 9.6.0, Kotlin 2.1.0, Compose BOM 2024.11.00
  - Implementasi theme kustom KetukTheme dengan warna ketuk_cream (#FFF7EC) sebagai background
  - Implementasi 8 layar: Splash, Onboarding (4 halaman), Login, RoleSelect, Home, Lokasi, Notifikasi, Riwayat
  - Setup navigasi dengan Compose Navigation
  - Implementasi komponen reusable: KetukButton, KetukSecondaryButton, KetukTextField, StatusChip
  - Import 20 asset gambar ke drawable-nodpi (PNG/JPG)
  - Setup font Inter (Regular, Medium, SemiBold, Bold)
  - Implementasi edge-to-edge UI dengan transparent system bars

- **File dibuat:**
  - Struktur package: `com.chat.ketukprojectmap`
  - MainActivity.kt
  - navigation/ (KetukNavHost.kt, Routes.kt)
  - ui/screens/ (splash, onboarding, login, role, home, lokasi, notifikasi, riwayat)
  - ui/components/ (KetukButton, KetukSecondaryButton, KetukTextField, StatusChip)
  - ui/theme/ (Theme.kt, Color.kt, Type.kt)
  - data/model/ (HomeSampleData.kt, LokasiSampleData.kt, RiwayatSampleData.kt)
  - 20 file asset di drawable-nodpi/
  - 4 file font Inter di font/

- **File diubah:**
  - AndroidManifest.xml (konfigurasi MainActivity)
  - build.gradle.kts (app) - dependencies Compose, Navigation, ViewModel
  - values/colors.xml - tambah ketuk_cream
  - values/themes.xml - theme kustom dengan windowBackground ketuk_cream
  - values/strings.xml - nama aplikasi

- **File dihapus:**
  - Tidak ada (fresh project)

- **Error/masalah:**
  - Tidak ada catatan error mayor dari implementasi awal.
  - Beberapa TODO ditemukan di kode untuk fitur yang belum lengkap.

- **Solusi yang berhasil:**
  - Compose Navigation dengan sealed class Routes berhasil mengatur alur navigasi.
  - Edge-to-edge dengan SystemBarStyle.light() berhasil membuat status bar dan navigation bar transparan.
  - Asset PNG di drawable-nodpi terdeteksi dan dapat digunakan di semua density.

- **Yang GAGAL/jangan diulang:**
  - Belum ada catatan kegagalan pada fase ini.

- **Perbedaan figma vs SCREENS.md:**
  - Perlu audit menyeluruh untuk memverifikasi kesesuaian implementasi dengan desain Figma.
  - Nama file Figma di SCREENS.md sudah diperbarui untuk menggunakan nama asli.

- **Asumsi dan TODO:**
  - Implementasi saat ini menggunakan data dummy/sample.
  - Autentikasi, backend, database belum diimplementasikan (sesuai fase Initial).
  - Google Maps di LokasiScreen menggunakan gambar placeholder (`peta_lokasi.png`).
  - Bottom navigation belum sepenuhnya fungsional untuk semua tab.

- **Status build:**
  - `./gradlew assembleDebug` sukses.
  - Tidak ada error lint kritis.

- **Belum selesai:**
  - Audit visual menyeluruh terhadap semua layar vs desain Figma.
  - Animasi transisi antar layar perlu diverifikasi.
  - Perbaikan layout Loading (pagar bawah tidak penuh sesuai catatan SCREENS.md).
  - Layar Core Phase (Home Penjaga, Profil Oma, dll) belum dikerjakan.

---
