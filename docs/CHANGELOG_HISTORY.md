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
