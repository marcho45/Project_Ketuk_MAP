# Project Summary - Ketuk Project MAP

**Terakhir diperbarui:** 2026-10-19 03:00 WIB (Tahap 2 Selesai)

---

## 1. Gambaran Proyek dan Peran Pengguna

**Ketuk** adalah aplikasi Android pemantau aktivitas harian orang tua (Oma/Opa) untuk keluarga dengan dukungan tetangga terdekat (Penjaga).

### Tiga Peran Pengguna:
1. **Wali (Keluarga):** Memantau status harian Oma/Opa, mengelola Circle Keluarga, melihat riwayat aktivitas & lokasi, serta menindaklanjuti eskalasi peringatan.
2. **Penjaga (Tetangga Terdekat):** Menerima permintaan ketuk pintu (eskalasi L3) saat Oma/Opa tidak merespons, memeriksa kondisi di rumah Oma/Opa, dan melaporkan hasilnya.
3. **Oma/Opa (Lansia yang Dipantau):** Melakukan pairing lewat QR Wali, menyetujui izin pemantauan, menggunakan tampilan Mode Pasif berhuruf besar, dan merespons sapaan L1 (`"Oma baik-baik saja?"`).

---

## 2. Tech Stack & Konfigurasi Build Variant (`demo` vs `full`)

### Platform & Versi Aktual (`app/build.gradle.kts` & `gradle/libs.versions.toml`)
- **Bahasa & Build:** Kotlin `2.2.10`, AGP `9.4.1`, Compose BOM `2026.02.01`
- **SDK:** `minSdk = 26`, `compileSdk = 37`, `targetSdk = 37`
- **Package Name:** `com.chat.ketukprojectmap` (variant `demo`: `com.chat.ketukprojectmap.demo`)

### Pemisahan Product Flavors (`flavorDimensions += "mode"`)
1. **`demo` (Khusus Pengumpulan UTS IF570L):**
   - `applicationIdSuffix = ".demo"`, `versionNameSuffix = "-demo"`
   - `buildConfigField("boolean", "DEMO_MODE", "true")`
   - Berjalan **100% offline in-memory** menggunakan `FakeAuthRepository` dan `DummyData` tanpa menginisialisasi `SupabaseProvider.client`.
2. **`full` (Khusus Kelanjutan UAS / Project Akhir):**
   - `buildConfigField("boolean", "DEMO_MODE", "false")`
   - Tetap menggunakan `SupabaseAuthRepository` (Supabase Auth, Postgrest `profiles`, verifikasi OTP email 6 digit, dan Google Sign-In native via `ComposeAuth`).

---

## 3. Arsitektur MVVM & Struktur Package

```
com.chat.ketukprojectmap/
├── MainActivity.kt
├── data/
│   ├── dummy/
│   │   ├── DummyModels.kt              # Data class Elder, CircleMember, Guardian, ActivityItem, dll.
│   │   └── DummyData.kt                # Data terpusat sesuai Figma 01-05 & 3 Akun Demo
│   ├── model/                          # Model data Home & Lokasi
│   │   ├── HomeSampleData.kt
│   │   ├── LokasiSampleData.kt
│   │   └── RiwayatSampleData.kt
│   ├── remote/
│   │   └── SupabaseProvider.kt         # Lazy SupabaseClient (hanya diakses saat DEMO_MODE == false)
│   └── repository/
│       ├── AuthRepository.kt           # Interface + factory tunggal via BuildConfig.DEMO_MODE
│       ├── SupabaseAuthRepository.kt   # Pembungkus kode Supabase Auth asli (100% utuh)
│       └── FakeAuthRepository.kt       # Autentikasi in-memory (delay 800ms) untuk 3 akun demo
├── navigation/
│   ├── KetukNavHost.kt                 # NavHost lengkap dengan route Setup Wali & detail_alert/{itemId}
│   └── Routes.kt                       # Konstanta route + helper homeRouteForRole & setupRouteForRole
└── ui/
    ├── components/
    │   ├── AuthComponents.kt
    │   ├── KetukButton.kt
    │   ├── KetukSecondaryButton.kt
    │   ├── KetukTextField.kt
    │   └── StatusChip.kt
    ├── screens/
    │   ├── splash/SplashScreen.kt
    │   ├── onboarding/ (OnboardingScreen.kt, OnboardingViewModel.kt)
    │   ├── login/ (LoginScreen.kt, LoginViewModel.kt)
    │   ├── register/ (RegisterScreen.kt, RegisterViewModel.kt)
    │   ├── verification/ (VerificationScreen.kt, VerificationViewModel.kt)
    │   ├── role/ (RoleSelectScreen.kt, RoleSelectViewModel.kt)
    │   ├── walisetup/WaliSetupScreens.kt   # (BARU Tahap 2) DetailOmaOpa, CircleKeluarga, QrWali+Loading, PilihPenjaga
    │   ├── detail/DetailAlertScreen.kt     # (BARU Tahap 2) Layar Detail Alert / Aktivitas (M8) dengan argumen itemId
    │   ├── home/ (HomeScreen.kt, HomeViewModel.kt)
    │   ├── riwayat/ (RiwayatScreen.kt, RiwayatViewModel.kt)
    │   ├── lokasi/ (LokasiScreen.kt, LokasiViewModel.kt)
    │   ├── notifikasi/NotifikasiScreen.kt
    │   └── profile/ (ProfileScreen.kt, ProfileViewModel.kt)
    └── theme/
        ├── Color.kt
        ├── Shape.kt                        # (BARU Tahap 2) KetukShapes kustom Material 3
        ├── Theme.kt                        # (DIUBAH Tahap 2) Light & Dark ColorScheme + KetukShapes
        └── Type.kt
```

---

## 4. Status Implementasi Layar (M1–M10)

| Kode | Layar | Peran | Status | Catatan |
|---|---|---|---|---|
| M1 | Splash & Onboarding (4 hal.) | Umum | ✅ Selesai | Mengecek sesi via `AuthRepository` |
| M2 | Login, Register, OTP | Umum | ✅ Selesai | Login punya hint 3 akun demo (`DEMO_MODE`), OTP 4 digit (`demo`) / 6 digit (`full`) |
| M3 | Pilih Peran ("Siapa kamu?") | Umum | 🟡 Sebagian | Peran Wali terhubung ke `detail_oma_opa`; Oma & Penjaga di Tahap 3–4 |
| Setup 1–4 | Detail Oma/Opa, Circle, QR Wali, Pilih Penjaga | Wali | ✅ Selesai | Sesuai `02. Initial Phase Wali.png` (validasi umur 50–120, Radio Button `Ibu Imas`) |
| M6 | Beranda Wali | Wali | ✅ Selesai | `rememberSaveable` tab, tombol kembali khusus `DEMO_MODE`, klik ke `DetailAlertScreen` |
| M7 | Riwayat Aktivitas | Wali | ✅ Selesai | `LazyColumn` 8 item `DummyData` + 4 filter chip + klik item ke `DetailAlertScreen(itemId)` |
| M8 | Detail Alert (`detail_alert/{itemId}`) | Wali | ✅ Selesai | Menerima `itemId`, status reaktif, & 3 tombol aksi dengan Snackbar mockup |
| M9 | Lokasi Oma Diyas, Notifikasi, & Profil Wali | Wali | ✅ Selesai | Foto `penjaga_lintang`, umur `67`, `NotifikasiScreen` 5 item, & kartu detail `ProfileScreen` |
| M4 | Pindai QR, Loading Pairing, Persetujuan & Izin | Oma/Opa | ❌ Tahap 3 | Akan dibuat di Tahap 3 sesuai `03. Initial Phase oma.png` |
| M5 | Mode Pasif Oma/Opa + Dialog Sapa L1 | Oma/Opa | ❌ Tahap 3 | Akan dibuat di Tahap 3 |
| M10 | Beranda, Lokasi, Riwayat Lokasi, Profil Penjaga | Penjaga | ❌ Tahap 4 | Akan dibuat di Tahap 4 sesuai `05. Core Phase Penjaga.png` |

---

## 5. Status Pemenuhan 7 Kriteria Rubrik UTS IF570L

| # | Kriteria Rubrik UTS | Bobot | Status Saat Ini | Catatan Progress |
|---|---|---|---|---|
| 1 | **Screen Implementation (>= 5 Jenis Layar)** | 15 Poin | 🟢 **Lengkap untuk Wali (Tahap 2/5)** | Sudah memiliki **List Screen** (`RiwayatScreen`), **Detail Screen** (`DetailAlertScreen(itemId)`), **Form/Input Screen** (`DetailOmaOpaScreen`, `LoginScreen`, `RegisterScreen`, `VerificationScreen`), **Native-Feature Mockup** (`QrWaliScreen` + Loading Pairing, `LokasiScreen`), dan **Free Choice** (`Splash`, `Onboarding`, `RoleSelect`, `CircleKeluarga`, `PilihPenjaga`, `HomeScreen`, `NotifikasiScreen`, `ProfileScreen`). Layar Oma/Opa & Penjaga menyusul di Tahap 3–4. |
| 2 | **UI Theming (Material 3 Custom Theme)** | 15 Poin | ✅ **Selesai (100%)** | `Color.kt`, `Type.kt` (`PlusJakartaSans`), `Shape.kt` (`KetukShapes`), dan `Theme.kt` (`KetukLightColorScheme` + `KetukDarkColorScheme`) aktif penuh. |
| 3 | **State Management** | 15 Poin | 🟢 **Sangat Baik** | ViewModel untuk Auth, Role, Home, Riwayat, Lokasi, Profil + `rememberSaveable` pada `selectedTab` di `HomeScreen` dan seluruh input form Setup Wali serta `DetailAlertScreen`. |
| 4 | **Navigation + Argument Passing** | 15 Poin | 🟢 **Sangat Baik** | `KetukNavHost.kt` mengirim dan mengonsumsi argumen `detail_alert/{itemId}` serta `verification?email={email}`, memiliki helper terpusat `homeRouteForRole` & `setupRouteForRole`, dan pengelolaan `popUpTo(..., inclusive = true)` yang bersih. |
| 5 | **Form Input & Validation** | 15 Poin | ✅ **Selesai (100%)** | `LoginScreen`, `RegisterScreen`, `VerificationScreen`, dan `DetailOmaOpaScreen` menggunakan `KeyboardType` yang tepat (`Email`, `Password`, `Number`), `isError = true`, `supportingText` merah, dan blokir submit saat tidak valid. |
| 6 | **Dummy Data Implementation** | 15 Poin | ✅ **Selesai (100%)** | Paket `data/dummy/` (`DummyModels.kt` & `DummyData.kt`) terhubung penuh ke seluruh layar Wali. |
| 7 | **Deliverables (`README.txt` & APK Demo)** | 10 Poin | 🟡 Tahap 5 | Build `assembleDemoDebug` & `assembleFullDebug` sukses; `README.txt` dibuat di Tahap 5. |

---

## 6. Daftar TODO & Tahap Berikutnya
- **Tahap 3:** Implementasi alur Oma/Opa (`Pindai QR`, `Loading Pairing`, `Persetujuan & Izin`, `Mode Pasif Oma/Opa` + simulasi Sapa L1 + tombol kembali khusus `DEMO_MODE` di Beranda Oma).
- **Tahap 4:** Implementasi alur Penjaga (`PenjagaHomeScreen` 3 tab: Beranda dengan banner interaktif + tombol kembali khusus `DEMO_MODE`, Lokasi dengan kontrol peta & layar `Riwayat Lokasi` 7 hari, serta Profil Penjaga).
- **Tahap 5:** Pembuatan `README.txt` UTS, audit akhir 7 kriteria rubrik, dan build final `assembleDemoDebug`.

