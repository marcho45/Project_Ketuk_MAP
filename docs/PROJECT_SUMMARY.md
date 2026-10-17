# Project Summary - Ketuk Project MAP

**Terakhir diperbarui:** 2026-10-17 16:27 UTC

---

## 1. Gambaran Proyek dan Peran Pengguna

**Ketuk** adalah aplikasi Android untuk pemantauan aktivitas orang tua (Oma/Opa) yang tinggal jauh dari keluarga. Aplikasi ini memungkinkan keluarga untuk memastikan keselamatan dan aktivitas harian Oma/Opa dengan bantuan tetangga terdekat.

### Tiga Peran Pengguna:

1. **Wali (Keluarga)**
   - Anggota keluarga yang ingin memantau Oma/Opa dari jarak jauh
   - Dapat melihat lokasi, status aktivitas, riwayat ketukan harian
   - Menerima notifikasi jika ada ketidaknormalan

2. **Penjaga (Tetangga Terdekat)**
   - Tetangga yang tinggal dekat dengan Oma/Opa
   - Bertugas mengecek kondisi fisik Oma/Opa jika diminta
   - Melaporkan hasil pemeriksaan ke Wali

3. **Oma/Opa (yang Dipantau)**
   - Orang tua yang akan dipantau
   - Melakukan "ketuk" harian sebagai tanda aktivitas normal
   - Dapat mengirim sinyal darurat jika dibutuhkan

### Alur Aplikasi:
```
Loading (Splash) 
  → Onboarding (4 halaman) 
  → Login (Google Sign-In)
  → Pilih Peran (Wali/Penjaga/Oma)
  → Home (berbeda per peran)
```

---

## 2. Tech Stack

### Platform & Bahasa
- **Platform:** Android
- **Bahasa:** Kotlin 2.1.0
- **Build System:** Gradle 9.6.0 (Kotlin DSL)
- **Package Name:** `com.chat.ketukprojectmap`

### Versi SDK
- **minSdk:** 23 (Android 6.0 Marshmallow)
- **targetSdk:** 35 (Android 15)
- **compileSdk:** 35

### UI Framework
- **Jetpack Compose:** 100% UI menggunakan Compose (tidak ada XML layout)
- **Compose BOM:** 2024.11.00
- **Material3:** Design system utama

### Library Utama
- **Navigation:** Compose Navigation (androidx.navigation:navigation-compose)
- **ViewModel:** Lifecycle ViewModel Compose
- **Core:** AndroidX Core KTX, Activity Compose
- **Font:** Inter (Regular, Medium, SemiBold, Bold) - custom TTF

---

## 3. Arsitektur dan Struktur Folder/Package

### Arsitektur
- **Pattern:** MVVM (Model-View-ViewModel)
- **UI:** Jetpack Compose dengan Material3
- **State Management:** ViewModel + Compose State
- **Navigasi:** Single Activity dengan Compose Navigation

### Struktur Package (`com.chat.ketukprojectmap`)

```
com.chat.ketukprojectmap/
├── MainActivity.kt                    # Single Activity entry point
├── navigation/
│   ├── KetukNavHost.kt               # Navigasi utama
│   └── Routes.kt                     # Sealed class route definitions
├── ui/
│   ├── screens/                      # Layar aplikasi
│   │   ├── splash/
│   │   │   └── SplashScreen.kt      # Loading screen
│   │   ├── onboarding/
│   │   │   ├── OnboardingScreen.kt
│   │   │   └── OnboardingViewModel.kt
│   │   ├── login/
│   │   │   └── LoginScreen.kt
│   │   ├── register/
│   │   │   ├── RegisterScreen.kt    # (BARU) Halaman daftar
│   │   │   └── RegisterViewModel.kt # (BARU) State + validasi
│   │   ├── verification/
│   │   │   └── VerificationScreen.kt
│   │   ├── role/
│   │   │   ├── RoleSelectScreen.kt
│   │   │   └── RoleSelectViewModel.kt
│   │   ├── home/
│   │   │   ├── HomeScreen.kt        # Home Wali
│   │   │   └── HomeViewModel.kt
│   │   ├── lokasi/
│   │   │   ├── LokasiScreen.kt
│   │   │   └── LokasiViewModel.kt
│   │   ├── notifikasi/
│   │   │   └── NotifikasiScreen.kt
│   │   └── riwayat/
│   │       ├── RiwayatScreen.kt
│   │       └── RiwayatViewModel.kt
│   ├── components/                   # Reusable components
│   │   ├── AuthComponents.kt        # (BARU) AuthField, AuthCloud
│   │   ├── KetukButton.kt           # Primary button (terakota)
│   │   ├── KetukSecondaryButton.kt  # Secondary button (outline)
│   │   ├── KetukTextField.kt        # Custom text input
│   │   └── StatusChip.kt            # Status badge
│   └── theme/
│       ├── Theme.kt                 # KetukTheme definition
│       ├── Color.kt                 # Color scheme
│       └── Type.kt                  # Typography (Inter font)
└── data/
    └── model/                        # Data models & sample data
        ├── HomeSampleData.kt
        ├── LokasiSampleData.kt
        └── RiwayatSampleData.kt
```

### Struktur Resource (`app/src/main/res/`)

```
res/
├── drawable/                         # Vector icons
│   ├── ic_launcher_foreground.xml
│   ├── ic_launcher_background.xml
│   └── splash_empty.xml
├── drawable-nodpi/                   # Asset gambar (PNG/JPG)
│   ├── logo_ketuk.png
│   ├── bg_desa.png
│   ├── pagar_bunga.png
│   ├── rumah_besar.png
│   ├── rumah_desa.png
│   ├── tok_melambai.png
│   ├── tok_memeriksa.png
│   ├── wali_dengan_tok.png
│   ├── oma_avatar.png
│   ├── oma_daun.png
│   ├── penjaga_lintang.jpg
│   ├── peta_lokasi.png
│   ├── btn_panah.png
│   ├── indikator_onboarding.png
│   ├── logo_google.png
│   └── nav_*.png (5 icon navigasi)
├── font/
│   ├── inter_regular.ttf
│   ├── inter_medium.ttf
│   ├── inter_semibold.ttf
│   └── inter_bold.ttf
├── mipmap-anydpi/                    # Launcher icon fallback (API 23-25)
│   ├── ic_launcher.xml
│   └── ic_launcher_round.xml
├── mipmap-anydpi-v26/                # Adaptive icon (API 26+)
│   ├── ic_launcher.xml
│   └── ic_launcher_round.xml
└── values/
    ├── colors.xml
    ├── strings.xml
    └── themes.xml
```

---

## 4. Daftar Layar + Status Implementasi

Dipetakan ke file Figma di `docs/figma/`:

| # | Layar | File Figma | Fase | Status | Catatan |
|---|---|---|---|---|---|
| 1 | Loading (Splash) | 01. Initial Phase (2).png | Initial | ✅ Ada | Perlu perbaikan: pagar bawah tidak penuh |
| 2-5 | Onboarding (4 hal.) | 01. Initial Phase (2).png | Initial | ✅ Ada | Perlu audit visual vs Figma |
| 6 | Login | 01. Initial Phase (2).png | Initial | ✅ Ada | Perlu audit visual vs Figma |
| 7 | Pilih Peran | 01. Initial Phase (2).png | Initial | ✅ Ada | Perlu audit visual vs Figma |
| 8 | Home Wali | 04. Core Phase Wali.png | Core | ✅ Ada | Perlu audit visual vs Figma |
| 9 | Lokasi | 04. Core Phase Wali.png | Core | ✅ Ada | Maps pakai placeholder image |
| 10 | Notifikasi | 04. Core Phase Wali.png | Core | ✅ Ada | Perlu audit visual vs Figma |
| 11 | Riwayat | 04. Core Phase Wali.png | Core | ✅ Ada | Perlu audit visual vs Figma |
| 12 | Home Penjaga | 05. Core Phase Penjaga.png | Core | ❌ Belum | Belum dikerjakan |
| 13 | Profil Oma | 03. Initial Phase oma.png | Initial | ❌ Belum | TODO: verifikasi desain |
| 14+ | Layar lain | - | - | ❌ Belum | Pairing, Alarm, dll tidak didesain |

---

## 5. Alur Navigasi

### Implementasi (Routes.kt)

```kotlin
sealed class Routes {
    data object Splash : Routes()
    data object Onboarding : Routes()
    data object Login : Routes()
    data object RoleSelect : Routes()
    data object Home : Routes()
    data object Lokasi : Routes()
    data object Notifikasi : Routes()
    data object Riwayat : Routes()
}
```

### Flow Navigasi:

```
Splash (auto 2s delay)
  → Onboarding (swipe 4 halaman, button "Mulai")
    → Login (button "Masuk dengan Google" + tombol back ke Onboarding)
      → RoleSelect (pilih Wali/Penjaga/Oma)
        → Home (berbeda per peran)
          ↔ Bottom Nav: Lokasi, Notifikasi, Riwayat
```

### Catatan Navigasi:
- **LoginScreen** memiliki tombol back (pojok kiri atas) dan BackHandler untuk kembali ke Onboarding
- Navigasi dari Login ke Onboarding menggunakan `popUpTo(Routes.login) { inclusive = true }` untuk membuang Login dari back stack
- Home untuk Penjaga dan Oma belum ada (saat ini semua role ke Home Wali)
- Bottom navigation sudah ada tapi tidak semua tab fungsional
- Back button handling perlu diverifikasi

---

## 6. Ringkasan Asset dan Pemakaian

Lihat `docs/ASSETS.md` untuk detail lengkap.

**Total Asset:** 20 file PNG/JPG di `drawable-nodpi/`

**Kategori:**
- Logo & Branding (2): logo_ketuk.png, logo_google.png
- Background (2): bg_desa.png, pagar_bunga.png
- Ilustrasi Bangunan (2): rumah_besar.png, rumah_desa.png
- Karakter (5): tok_melambai.png, tok_memeriksa.png, wali_dengan_tok.png, oma_avatar.png, oma_daun.png
- Profil (1): penjaga_lintang.jpg
- Maps (1): peta_lokasi.png
- UI Elements (2): btn_panah.png, indikator_onboarding.png
- Navigation (5): nav_home.png, nav_lokasi.png, nav_notifikasi.png, nav_profil.png, nav_riwayat.png

---

## 7. Design System

### Warna Utama (Color.kt & colors.xml)

```kotlin
// Custom Ketuk Colors
KetukCream = Color(0xFFFFF7EC)         // Background utama
KetukTerakota = Color(0xFFD27857)      // Primary button
KetukDarkGreen = Color(0xFF2D5F2E)     // Accent
KetukLightGreen = Color(0xFFE8F4E8)    // Background secondary
```

### Tipografi (Type.kt)

**Font Family:** Inter (Regular/Medium/SemiBold/Bold)

**Text Styles:** Display (32sp), Headline (24sp), Title (20sp), Body (16sp), Label (14sp), Caption (12sp)

### Bentuk (Shape)

- **Button Primary:** RoundedCornerShape(100.dp) - pill shape
- **Card:** RoundedCornerShape(16.dp)
- **TextField:** RoundedCornerShape(12.dp)

### Spacing: 8dp, 12dp, 16dp, 24dp. Button Height: 48dp (min touch target)

### Komponen Reusable

1. **KetukButton** - Background: KetukTerakota, Text: White SemiBold, Pill shape
2. **KetukSecondaryButton** - Outline, Text: KetukTerakota
3. **KetukTextField** - White background, 12dp radius
4. **StatusChip** - Dynamic color per status

---

## 8. Konfigurasi Penting dan Alasannya

### ⚠️ PENTING - Launcher Icon:
- Icon launcher WAJIB di folder `mipmap/`, bukan `drawable/`
- Warning IDE "vector should be in drawable" untuk `mipmap-anydpi/ic_launcher.xml` **SENGAJA DIABAIKAN**
- Alasan: AndroidManifest merujuk `@mipmap/`, pindah ke drawable akan error atau icon tidak muncul
- Fallback vector di `mipmap-anydpi/` adalah solusi sah untuk API 23-25

### MainActivity - Edge-to-Edge
- System bars transparan untuk tampilan full screen
- Background cream terlihat dari atas sampai bawah
- Gunakan `systemBarsPadding()` di Composable untuk hindari konten tertutup

### Theme
- Parent: `android:Theme.Material.Light.NoActionBar`
- Window Background: `@color/ketuk_cream` (#FFF7EC)
- No ActionBar: Custom top bar di setiap screen

---

## 9. Masalah yang Masih Terbuka dan TODO

### Masalah Implementasi:

1. **Loading Screen** - Pagar bunga di bawah tidak memenuhi lebar layar
2. **Audit Visual** - Semua layar perlu dibandingkan dengan Figma
3. **Navigasi** - Home untuk Penjaga dan Oma belum dibuat
4. **Maps** - LokasiScreen pakai placeholder image
5. **TODO di Kode** - 6 TODO comment di LokasiScreen, HomeScreen, RoleSelectScreen

## 10. Pelajaran dan Kesalahan yang Jangan Diulang

### ✅ Yang Berhasil (Harus Dipertahankan):

1. **Launcher Icon Vector Fallback**
   - `mipmap-anydpi/ic_launcher.xml` untuk API 23-25 adalah solusi benar
   - JANGAN pindahkan ke `drawable/` meskipun IDE warning
   - AndroidManifest HARUS tetap merujuk `@mipmap/ic_launcher`

2. **Compose Navigation dengan Sealed Class**
   - Pattern `sealed class Routes` bersih dan type-safe
   - Mudah di-maintain dan di-extend

3. **Edge-to-Edge dengan Transparent System Bars**
   - `enableEdgeToEdge()` + `systemBarsPadding()` bekerja baik

4. **Asset di drawable-nodpi**
   - PNG/JPG terdeteksi di semua density
   - Tidak perlu duplikasi untuk asset ilustrasi

### ❌ Yang GAGAL (Jangan Diulang):

1. **JANGAN memindahkan vector launcher icon dari mipmap ke drawable**
   - Menyebabkan manifest error atau icon tidak muncul

2. **JANGAN membuat layout XML baru**
   - Proyek 100% Compose, konsistensi harus dijaga

3. **JANGAN hardcode warna di banyak tempat**
   - Gunakan MaterialTheme atau Color.kt

### ⚠️ Hal Perlu Diperhatikan:

1. **minSdk 23 Compatibility** - Selalu cek API level untuk fitur baru
2. **Asset Naming** - WAJIB snake_case, huruf kecil, a-z/0-9/underscore
3. **File Figma Naming** - Selalu verifikasi nama file di `docs/figma/`
4. **State Management** - LaunchedEffect key benar, gunakan rememberSaveable

---

## 11. Tanggal Update Terakhir

**Terakhir diperbarui:** 2026-10-13 01:45 WIB

**Update terakhir:**
- Membuat dokumentasi lengkap struktur proyek
- Membuat CHANGELOG_HISTORY.md dengan 2 entri
- Membuat PROJECT_SUMMARY.md ini
- Status: Dokumentasi awal selesai, siap untuk development berikutnya

---

### Fitur Belum Diimplementasikan:
Autentikasi, Backend/API, Database, Push notification, Real-time location, Alarm, Pairing, Profile, Settings

### Layar Belum Dibuat:
Home Penjaga, Profil Oma, Pairing, Alarm Aktif, Tugas Penjaga Detail, Sinkron, dll

---


**Font:**
- Inter Family (4 weights: Regular/Medium/SemiBold/Bold) di `res/font/`

**Launcher Icon:**
- Adaptive Icon (API 26+): `mipmap-anydpi-v26/`
- Vector Fallback (API 23-25): `mipmap-anydpi/`

---
