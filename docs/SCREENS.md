# Spesifikasi Layar & Pemetaan Rubrik UTS (dari Figma)

**Terakhir diperbarui:** 2026-10-19 (Tugas Khusus: Tombol Tema Manual via `SharedPreferences` & Pembersihan Dark Mode Semantik di Seluruh Layar)

Gambar acuan utama ada di `docs/figma/` dan alur navigasi mengacu pada `docs/figma/flowchart.png`.
Jika teks spesifikasi berbeda dengan gambar Figma, **ikuti gambar Figma** dan catat di bagian "Perbedaan dengan Figma".

---

## 1. Pemetaan File Figma (`docs/figma/`)

| File Figma Asli | Isi Layar | Fase | Peran |
|---|---|---|---|
| `01. Initial Phase (2).png` | Splash (M1), Onboarding 1-4 (M1), Login (M2), OTP 4 digit (M2), Pilih Peran (M3) | Initial Phase | Umum |
| `02. Initial Phase Wali.png` | Pilih Peran (Wali terpilih), Detail Oma/Opa, Circle Keluarga, QR Wali Oma Diyas, Loading Pairing, Pilih Penjaga Terdekat | Initial Phase | Wali |
| `03. Initial Phase oma.png` | Pilih Peran (Oma/Opa terpilih), Pindai QR (M4), Loading Pairing (M4), Persetujuan & Izin (M4) | Initial Phase | Oma/Opa |
| `04. Core Phase Wali.png` | Beranda Wali (M6), Riwayat Aktivitas (M7), Lokasi Oma Diyas (M9), Tab Profil Wali | Core Phase | Wali |
| `05. Core Phase Penjaga.png` | Beranda Penjaga (M10), Lokasi Oma Diyas - Penjaga (M10) | Core Phase | Penjaga |
| `flowchart.png` | Diagram alur M1–M10 untuk Sistem, Oma/Opa, Wali, dan Penjaga | Semua Fase | Semua Peran |

---

## 2. Tabel Status Seluruh Layar & Kategori Rubrik UTS

| Kode | Layar | Peran | File Figma | File Kotlin | Route | Jenis Layar UTS | Status Saat Ini |
|---|---|---|---|---|---|---|---|
| M1 | Splash Screen | Umum | `01. Initial Phase (2).png` | `SplashScreen.kt` | `splash` | Free Choice | ✅ Sesuai Figma (Light & Dark Mode) |
| M1 | Onboarding (4 hal.) | Umum | `01. Initial Phase (2).png` | `OnboardingScreen.kt` | `onboarding` | Free Choice | ✅ Sesuai Figma (Light & Dark Mode) |
| M2 | Login | Umum | `01. Initial Phase (2).png` | `LoginScreen.kt`, `LoginViewModel.kt` | `login` | **Form/Input** | ✅ Sesuai Figma (+ Hint Demo, `isError`/`supportingText`, Light & Dark Mode) |
| M2 | Register (Daftar) | Umum | Gaya `01. Initial Phase (2).png` | `RegisterScreen.kt`, `RegisterViewModel.kt` | `register` | **Form/Input** | ✅ Sesuai Spesifikasi (Light & Dark Mode) |
| M2 | Verifikasi OTP | Umum | `01. Initial Phase (2).png` | `VerificationScreen.kt`, `VerificationViewModel.kt` | `verification?email={email}` | **Form/Input** | ✅ Sesuai Figma (4 digit di `demo`, 6 digit di `full`, Light & Dark Mode) |
| M3 | Pilih Peran ("Siapa kamu?") | Umum | `01` / `02` / `03` | `RoleSelectScreen.kt`, `RoleSelectViewModel.kt` | `role` | Free Choice | ✅ Wali terhubung ke `detail_oma_opa` (Light & Dark Mode) |
| Setup 1 | Detail Oma/Opa | Wali | `02. Initial Phase Wali.png` | `WaliSetupScreens.kt` (`DetailOmaOpaScreen`) | `detail_oma_opa` | **Form/Input** | ✅ Sesuai Figma (Validasi nama & umur 50–120 + badge `"Tahun"`, Light & Dark Mode) |
| Setup 2 | Circle Keluarga | Wali | `02. Initial Phase Wali.png` | `WaliSetupScreens.kt` (`CircleKeluargaScreen`) | `circle_keluarga` | Free Choice | ✅ Sesuai Figma (3 anggota, tambah anggota, QR tetap putih agar bisa dipindai, Light & Dark Mode) |
| Setup 3 | QR Wali Oma Diyas | Wali | `02. Initial Phase Wali.png` | `WaliSetupScreens.kt` (`QrWaliScreen`) | `qr_wali` | **Native Mockup** | ✅ Sesuai Figma (`qr_pairing` tetap berlatar putih + tombol simulasi pairing, Light & Dark Mode) |
| Setup 3b | Loading Pairing (Wali) | Wali | `02. Initial Phase Wali.png` | `WaliSetupScreens.kt` (`LoadingPairingWaliContent`) | *(state di `qr_wali`)* | **Native Mockup** | ✅ Sesuai Figma (`qr_pairing_blur` + `ic_spinner` 2 detik) |
| Setup 4 | Pilih Penjaga Terdekat | Wali | `02. Initial Phase Wali.png` | `WaliSetupScreens.kt` (`PilihPenjagaScreen`) | `pilih_penjaga` | Free Choice | ✅ Sesuai Figma (Search bar + Radio Button + `"Ibu Imas"`, Light & Dark Mode) |
| M6 | Beranda Wali | Wali | `04. Core Phase Wali.png` | `HomeScreen.kt` (Tab 0) | `home` | Free Choice | ✅ Sesuai Figma (+ Tombol panah kembali khusus `DEMO_MODE`, Light & Dark Mode) |
| M7 | Riwayat Aktivitas | Wali | `04. Core Phase Wali.png` | `RiwayatScreen.kt`, `RiwayatViewModel.kt` (Tab 1) | `home` (Tab 1) | **List Screen** | ✅ Sesuai Figma (`LazyColumn` 8 item + 4 filter chip + klik ke M8, Light & Dark Mode) |
| M8 | Detail Alert / Aktivitas | Wali | `flowchart.png` *(tanpa frame khusus)* | `DetailAlertScreen.kt` | `detail_alert/{itemId}` | **Detail Screen** | ✅ Selesai (Menerima `itemId`, status reaktif, 3 tombol aksi, Light & Dark Mode) |
| M9 | Lokasi Oma Diyas (Wali) | Wali | `04. Core Phase Wali.png` | `LokasiScreen.kt` (Tab 2) | `home` (Tab 2) | **Native Mockup** | ✅ Sesuai Figma (Umur `67`, `penjaga_lintang`, Snackbar aksi, Light & Dark Mode) |
| - | Notifikasi | Wali/Penjaga | *(Ikon lonceng M6/M10)* | `NotifikasiScreen.kt` | `notifikasi` | Free Choice | ✅ Selesai (`LazyColumn` 5 item `NotificationItem` + klik ke M8, Light & Dark Mode) |
| - | Tab Profil Wali | Wali | `04. Core Phase Wali.png` | `ProfileScreen.kt` (Tab 3) | `home` (Tab 3) | Free Choice | ✅ Selesai (Kartu akun Wali, kartu lansia, Switch `"Mode gelap"`, & tombol Keluar) |
| M4 | Pindai QR | Oma/Opa | `03. Initial Phase oma.png` | *(Tahap 3)* | *(Tahap 3)* | **Native Mockup** | ❌ Belum (Tahap 3) |
| M4 | Loading Pairing (Oma) | Oma/Opa | `03. Initial Phase oma.png` | *(Tahap 3)* | *(Tahap 3)* | **Native Mockup** | ❌ Belum (Tahap 3) |
| M4 | Persetujuan & Izin | Oma/Opa | `03. Initial Phase oma.png` | *(Tahap 3)* | *(Tahap 3)* | **Native Mockup** | ❌ Belum (Tahap 3) |
| M5 | Mode Pasif Oma/Opa | Oma/Opa | `flowchart.png` *(tanpa frame khusus)* | *(Tahap 3)* | *(Tahap 3)* | Free Choice | ❌ Belum (Tahap 3) |
| M10 | Beranda Penjaga | Penjaga | `05. Core Phase Penjaga.png` | *(Tahap 4)* | *(Tahap 4)* | Free Choice | ❌ Belum (Tahap 4) |
| M10 | Lokasi Oma Diyas (Penjaga) | Penjaga | `05. Core Phase Penjaga.png` | *(Tahap 4)* | *(Tahap 4)* | **Native Mockup** | ❌ Belum (Tahap 4) |
| - | Riwayat Lokasi (7 Hari) | Penjaga | *(Dari kartu Riwayat Lokasi M10)* | *(Tahap 4)* | *(Tahap 4)* | Free Choice | ❌ Belum (Tahap 4) |
| - | Tab Profil Penjaga | Penjaga | `05. Core Phase Penjaga.png` | *(Tahap 4)* | *(Tahap 4)* | Free Choice | ❌ Belum (Tahap 4) |

---

## 3. Rincian Spesifikasi Visual per Layar

### A. Layar Umum (`01. Initial Phase (2).png`)
1. **Splash (`SplashScreen.kt`):** Latar semantik (`KetukTheme.colors.screenBackground`), `logo_ketuk`, judul `"Ketuk"`, tagline `"Kalau HP orang tersayang hening, ada yang mengetuk pintu."`, `rumah_besar` & `pagar_bunga` berulang di bawah. Mengecek `authRepository.hasSession()` & `getRole()`.
2. **Onboarding (`OnboardingScreen.kt`):** 4 halaman `HorizontalPager` dengan `bg_desa` + `tok_melambai`, indikator pil animasi, tombol `"Skip"` & panah, serta tombol `"Mulai"` di halaman ke-4.
3. **Login (`LoginScreen.kt`):**
   - Maskot `tok_memeriksa` + `asset_ketuk_daun`, `logo_ketuk` + `"Ketuk"`, subjudul `"Halo! Senang melihatmu kembali"`.
   - Field Email (`KeyboardType.Email`) & Password (`KeyboardType.Password`) dengan validasi `isError = true` dan `supportingText` merah.
   - Tombol `"Masuk"`, pemisah `"atau"`, tombol `"Lanjut dengan Google"`, tautan `"Belum punya akun? Daftar disini"`.
   - Khusus `BuildConfig.DEMO_MODE == true`: Kartu hint 3 akun demo (`wali@ketuk.app`, `oma@ketuk.app`, `penjaga@ketuk.app`, sandi `Ketuk123`) yang dapat diketuk untuk mengisi otomatis.
4. **Register (`RegisterScreen.kt`):** 5 field (Nama Lengkap, Email, Nomor HP hanya angka 9-13 digit, Password min 8 karakter, Konfirmasi Password) dengan validasi lengkap.
5. **Verifikasi OTP (`VerificationScreen.kt`):** `bg_desa` + `maskot_hp`, 4 kotak digit di mode `demo` (6 kotak di mode `full`), tombol `"Confirm"`, dan `"Didn't receive the code? Resend"`.
6. **Pilih Peran (`RoleSelectScreen.kt`):** Judul `"Siapa kamu?"`, subjudul `"Pilih peranmu untuk melanjutkan"`, 3 kartu horizontal (`Saya wali Oma dan Opa`, `Saya penjaga Oma dan Opa`, `Saya adalah Oma/Opa`), tombol `"Lanjutkan"`.

### B. Alur Setup & Core Wali (`02. Initial Phase Wali.png` & `04. Core Phase Wali.png`)
- **Detail Oma/Opa (`DetailOmaOpaScreen`):** Input nama Wali (`"Nanda Valeri"`), toggle pill `"Opa"` / `"Oma"`, input nama lansia (`"Sri Diyas Anjarwati"`), input umur (`"67"`) dengan badge terakota `"Tahun"`, serta validasi lengkap (`isError = true`, `supportingText`, blokir submit).
- **Circle Keluarga (`CircleKeluargaScreen`):** Header `"3 Anggota"`, list `Nanda (Wali)` (Ketua + mahkota), `Papa` (Anggota), `Mama` (Anggota), tombol `+ Tambah Anggota` (membuka dialog tambah anggota in-memory), kartu `"Undang Keluarga Lain"` dengan QR (`qr_pairing` di atas `qrSurface` putih agar tetap dapat dipindai di mode gelap), tombol `"Bagikan QR"`, `"Salin Tautan"`, dan `"Lanjutkan"`.
- **QR Wali Oma Diyas & Loading Pairing (`QrWaliScreen`):** Kartu QR besar (berlatar `qrCardWali`/`qrSurface` terang agar QR tetap bisa dipindai) + simulasi pindah ke layar blur `qr_pairing_blur` + `ic_spinner` `"Loading Pairing..."` (2 detik) -> lanjut ke Pilih Penjaga.
- **Pilih Penjaga Terdekat (`PilihPenjagaScreen`):** Search bar `"Cari nama atau nomor..."`, daftar kontak dengan Radio Button (`Pak Andy`, `Ibu Imas`, `Adik Adam`, `Pak RT`), tombol `"Pilih sebagai Penjaga"` -> masuk ke `HomeScreen` dengan membersihkan stack setup.
- **Core Wali (`HomeScreen` - 4 Tab Bottom Bar dengan `rememberSaveable`):**
  - **Tab 0 (Beranda - M6):** Header `"Halo, Nandaa"` (dilengkapi tombol panah kembali khusus `BuildConfig.DEMO_MODE == true` untuk menengok layar Pilih Peran), kartu Oma Diyas, Status hari ini (`"Lihat detail >"` membuka `DetailAlertScreen("act_2")`), 3 kartu Aksi cepat dengan Snackbar mockup, dan Timeline Aktivitas terbaru yang bisa diklik ke `DetailAlertScreen`.
  - **Tab 1 (Riwayat Aktivitas - M7):** `LazyColumn` 8 item dari `DummyData.initialActivities` + 4 filter chip (`Semua`, `Aktivitas`, `HP`, `Sapa`). Klik item membuka `DetailAlertScreen(item.id)`.
  - **Tab 2 (Lokasi Oma Diyas - M9):** Peta statis `peta_lokasi`, bottom sheet info Oma Diyas (`67 tahun`), tombol `"Lihat Rute"` & `"Hubungi Oma"` (menampilkan Snackbar simulasi), serta kartu Penjaga terdekat (`Markolintang`, `170 m`) dengan foto `penjaga_lintang`.
  - **Tab 3 (Profil Wali):** Kartu akun Wali (`Nanda Valeri`, `wali@ketuk.app`), kartu Lansia yang dipantau (`Oma Diyas`, `67 Tahun`), kartu switch `"Mode gelap"` (`Icons.Rounded.LightMode`/`DarkMode`, tersimpan di `SharedPreferences` `"dark_theme"`, ditandai `// TODO(tim): tidak ada di Figma`), dan tombol `"Keluar"` + dialog konfirmasi.
- **Detail Alert / Aktivitas (`DetailAlertScreen` - M8):** Menerima argumen `itemId` dari NavHost (`detail_alert/{itemId}`), menampilkan level eskalasi (`NORMAL` / `L1` / `L2` / `L3`), waktu, status reaktif, deskripsi lengkap, lokasi Oma Diyas, dan 3 tombol aksi (`"Telepon Oma"`, `"Konfirmasi Oma Aman"`, `"Minta Penjaga Mengecek"`).

### C. Alur Oma/Opa (`03. Initial Phase oma.png` & Flowchart M4–M5)
- **Pindai QR (M4):** Judul `"Pindai QR"`, mockup kamera dengan bingkai scan + kartu petunjuk `"Arahkan ke QR code di HP wali"` + tombol `"Simulasikan Pindai"`.
- **Loading Pairing (M4):** Latar blur `qr_pairing_blur` + spinner biru + `"Loading Pairing..."` (delay otomatis 2 detik).
- **Persetujuan & Izin (M4):** Kartu `"Yang dipantau"` (3 item), kartu `"Izin yang diperlukan"` (4 poin bernomor), persetujuan privasi yang mengaktifkan tombol `"Saya Setuju"`.
- **Mode Pasif Oma/Opa (M5):** Tampilan ramah lansia huruf besar + simulasi kartu/dialog Sapa L1 `"Oma baik-baik saja?"` (`"Saya baik-baik saja"` / `"Butuh bantuan"`) + tombol `"Keluar"`.

### D. Alur Penjaga (`05. Core Phase Penjaga.png` & Flowchart M10)
- **Core Penjaga (3 Tab Bottom Bar: Beranda, Lokasi, Profil):**
  - **Beranda (M10):** Header `"Halo, Markolintang"` (`"Penjaga terdekat - Oma Diyas"`), banner merah muda `"Tolong ketuk pintu Oma Diyas"` dengan 2 tombol interaktif `"Oma aman"` & `"Butuh bantuan"`, kartu peta mini `"Rumah Oma Diyas - Jl. Syeckh Nawawi Albantani 13"`, 2 kartu Aksi cepat (`Lacak`, `Hubungi`), kartu Riwayat (`"Pengecekan berhasil - Oma Diyas - 13 hari yang lalu"`).
  - **Lokasi (M10):** Kartu atas `"Oma Diyas 170 m darimu"` (`"Di rumah"`, badge `"Aman - Aktivitas Normal"`), peta statis dengan tombol kompas & `+/-`, badge `"Lokasi terakhir diperbarui Hari ini, 18.55"`, kartu `"Riwayat Lokasi"` -> membuka daftar riwayat lokasi 7 hari.
  - **Profil Penjaga:** Info akun Markolintang & tombol `"Keluar"`.

---

## 4. Perbedaan dengan Figma yang Sudah Disepakati Tim
- **[2026-10-19] Field Login:** Gambar Figma `01` menampilkan label `"Nomor HP"`, namun aplikasi menggunakan `"Email"` agar kompatibel dengan Supabase Auth (mode `full`) dan 3 akun demo (`wali@ketuk.app`, `oma@ketuk.app`, `penjaga@ketuk.app`).
- **[2026-10-19] Nama Calon Penjaga ke-2:** Mengikuti gambar Figma `02. Initial Phase Wali.png` yaitu **`"Ibu Imas"`** (`+62 812 4987 1153`) dan komponen pemilihannya menggunakan **Radio Button** (pilih satu) sesuai visual Figma.
- **[2026-10-19] Umur Oma Diyas:** Di Figma `02` tertulis `67` tahun sedangkan di Figma `04` tertulis `72 tahun`. Diseragamkan menjadi **`67` tahun** di `DummyData.kt` sebagai satu sumber data tunggal.
- **[2026-10-19] Kotak OTP:** Menggunakan 4 kotak digit di mode `demo` (sesuai Figma `01`) dan 6 kotak digit di mode `full` (sesuai default OTP email Supabase).
- **[2026-10-19] Tombol Panah Kembali Khusus Mode Demo di Beranda Wali (`HomeScreen`):** Selisih dari Figma yang disengaja (`// TODO(tim): tombol khusus demo, tidak ada di Figma`). Hanya muncul saat `BuildConfig.DEMO_MODE == true` dan tab Beranda (`selectedTab == 0`) aktif, sejajar di sebelah kiri sapaan `"Halo, Nandaa"` tanpa menggeser ikon lonceng notifikasi, agar dosen penguji dapat kembali ke layar Pilih Peran (`Routes.role`) dengan bersih (`popUpTo(Routes.home) { inclusive = true }`). Pada varian `full`, tampilan Beranda tidak berubah sama sekali.
- **[2026-10-19] Tombol Manual Mode Gelap (`ProfileScreen`) & Sistem Warna Semantik (`KetukTheme.colors`):** Selisih dari Figma yang disengaja (`// TODO(tim): tidak ada di Figma`). Tema aplikasi tidak lagi mengikuti tema sistem HP secara otomatis (`isSystemInDarkTheme()` dihapus dari parameter default `KetukTheme`), melainkan dikendalikan lewat Switch `"Mode gelap"` di bawah kartu info akun pada `ProfileScreen` Wali (disimpan ke `SharedPreferences` `"ketuk_prefs"` kunci `"dark_theme"`, default `false` = terang). Mode terang tetap 100% identik dengan desain asli, sedangkan mode gelap memakai palet hangat gelap (`KetukTheme.colors`) dengan permukaan QR tetap putih (`qrSurface`) agar tetap dapat dipindai.
- **[2026-10-19] Layar Tanpa Frame Figma Khusus (Ditandai `// TODO(tim)`):** `DetailAlertScreen` (M8), `NotifikasiScreen`, dan `ProfileScreen` Wali dirancang mengikuti sistem warna (`KetukTheme.colors`) dan tipografi `PlusJakartaSans` aplikasi Ketuk.