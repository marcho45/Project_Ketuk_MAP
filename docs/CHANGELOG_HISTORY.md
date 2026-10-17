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
