======================================================================
1. DESKRIPSI SINGKAT APLIKASI
======================================================================
Ketuk adalah aplikasi Android berbasis Jetpack Compose untuk membantu
keluarga memantau aktivitas dan keamanan harian orang tua lansia
(Oma/Opa) dari jarak jauh tanpa mengganggu privasi mereka. Aplikasi
ini menghubungkan tiga peran utama: Wali (keluarga inti), Oma/Opa
(lansia yang dipantau), dan Penjaga Terdekat (tetangga dalam radius
dekat). Saat aktivitas HP Oma/Opa tidak terdeteksi, sistem menjalankan
eskalasi bertahap mulai dari sapaan mandiri (L1), peringatan ke Wali
(L2), hingga permintaan ketuk pintu rumah kepada Penjaga (L3). Pada
build variant demoDebug untuk UTS IF570L, seluruh alur ketiga peran
berjalan 100% offline in-memory menggunakan data simulasi terpusat.

======================================================================
2. ANGGOTA KELOMPOK
======================================================================
1. Lintang Balakosa Ardhana (126931)
2. Nanda Valeri (127234)
3. Paulus Marchionni Sadipun (126931)

======================================================================
3. CARA MENJALANKAN DAN AKUN DEMO
======================================================================
Spesifikasi Build (app/build.gradle.kts):
- Build Variant UTS : demoDebug (flavor: demo, buildType: debug)
- Perintah Build    : ./gradlew assembleDemoDebug
- Nama File APK     : app-demo-debug.apk
  (app/build/outputs/apk/demo/debug/app-demo-debug.apk)
- Application ID    : com.chat.ketukprojectmap.demo
- SDK               : minSdk = 26, targetSdk = 37, compileSdk = 37

Akun Demo Login (DummyData.kt & LoginScreen.kt):
Di halaman Login (LoginScreen), ketuk salah satu baris pada kartu
"Akun Demo UTS" untuk mengisi email dan sandi otomatis, atau ketik:
1. Peran Wali (Keluarga) - Nanda Valeri
   - Email / No. HP : wali@ketuk.app / 081234567890
   - Password       : Ketuk123
   - Tujuan         : Langsung membuka HomeScreen Wali (4 tab)
2. Peran Oma/Opa - Sri Diyas Anjarwati
   - Email / No. HP : oma@ketuk.app / 081298765432
   - Password       : Ketuk123
   - Tujuan         : Langsung membuka OmaPassiveScreen
3. Peran Penjaga Terdekat - Markolintang
   - Email / No. HP : penjaga@ketuk.app / 081355557777
   - Password       : Ketuk123
   - Tujuan         : Langsung membuka GuardianHomeScreen (3 tab)

Cara Mencoba Alur Setup Peran (Wizard Awal):
- Klik tombol "Lanjut dengan Google" (simulasi) di LoginScreen untuk
  langsung menuju RoleSelectScreen ("Siapa kamu?"), atau
- Klik "Daftar disini" (RegisterScreen), isi data valid, lalu pada
  VerificationScreen masukkan 6 digit angka apa saja untuk lanjut ke
  RoleSelectScreen dan mengikuti setup Wali / Oma/Opa / Penjaga.

======================================================================
4. PEMETAAN KE RUBRIK UTS
======================================================================
A. Implementasi Jenis Layar (Screen Implementation):
1. List Screen:
   - RiwayatScreen.kt (RiwayatScreen: LazyColumn + 4 filter chip)
   - GuardianLocationHistoryScreen.kt (GuardianLocationHistoryScreen:
     LazyColumn riwayat pergerakan 7 hari terakhir)
   - NotifikasiScreen.kt (NotifikasiScreen: LazyColumn 5 notifikasi)
2. Detail Screen:
   - DetailAlertScreen.kt (DetailAlertScreen: merender detail item
     secara dinamis berdasarkan argumen route itemId)
3. Form / Input Screen:
   - LoginScreen.kt (LoginScreen & LoginContent)
   - RegisterScreen.kt (RegisterScreen)
   - VerificationScreen.kt (VerificationScreen: 6 kotak kode OTP)
   - WaliSetupScreens.kt (DetailOmaOpaScreen: form profil & umur)
4. Native-Feature UI Mockup:
   - WaliSetupScreens.kt (QrWaliScreen & LoadingPairingWaliContent)
   - OmaScreens.kt (ScanQrScreen, LoadingPairingOmaScreen,
     OmaPermissionsScreen)
   - LokasiScreen.kt (LokasiScreen: peta statis & bottom sheet)
   - GuardianLokasiScreen.kt (GuardianLokasiScreen: peta & kontrol)
5. Pilihan Bebas (Free Choice Screen):
   - SplashScreen.kt (SplashScreen)
   - OnboardingScreen.kt (OnboardingScreen: HorizontalPager 4 hal.)
   - RoleSelectScreen.kt (RoleSelectScreen)
   - WaliSetupScreens.kt (CircleKeluargaScreen, PilihPenjagaScreen)
   - HomeScreen.kt (HomeScreen: beranda pemantauan Wali)
   - ProfileScreen.kt (ProfileScreen: profil Wali & mode gelap)
   - OmaScreens.kt (OmaPassiveScreen: layar aktif ramah lansia)
   - GuardianHomeScreen.kt (GuardianHomeScreen: beranda Penjaga)
   - GuardianProfileScreen.kt (GuardianProfileScreen: profil Penjaga)

B. Komponen Teknis Rubrik:
- UI Theming (Material 3 + Light/Dark Mode):
  ui/theme/Color.kt, ui/theme/Type.kt, ui/theme/Shape.kt,
  ui/theme/Theme.kt, ui/components/KetukDarkModeCard.kt
- State Management (ViewModel + StateFlow + rememberSaveable):
  LoginViewModel.kt, RegisterViewModel.kt, VerificationViewModel.kt,
  RoleSelectViewModel.kt, OnboardingViewModel.kt, HomeViewModel.kt,
  RiwayatViewModel.kt, LokasiViewModel.kt, ProfileViewModel.kt,
  GuardianViewModel.kt
- Navigation & Pengiriman Argumen:
  navigation/KetukNavHost.kt (KetukNavHost), navigation/Routes.kt
  (route berargumen detail_alert/{itemId} dan
  verification?email={email})
- Form Input & Validation (KeyboardType + isError + blokir submit):
  LoginScreen.kt, RegisterScreen.kt, RegisterViewModel.kt,
  VerificationScreen.kt, VerificationViewModel.kt,
  WaliSetupScreens.kt (DetailOmaOpaScreen),
  ui/components/AuthComponents.kt, ui/components/KetukTextField.kt
- Dummy Data Terpusat:
  data/dummy/DummyModels.kt, data/dummy/DummyData.kt,
  data/model/HomeSampleData.kt, data/model/LokasiSampleData.kt,
  data/model/RiwayatSampleData.kt,
  data/repository/FakeAuthRepository.kt

======================================================================
5. PEMBAGIAN TUGAS DAN SISTEM KERJA
======================================================================
Pembagian tugas : Dibagi rata berdasarkan alur layar dan komponen
                  teknis. Lintang memegang alur Wali, custom theme,
                  dan animasi. Nanda memegang alur Autentikasi dan
                  Oma/Opa, dummy data, serta validasi form. Paulus
                  memegang alur Penjaga, lokasi, detail, navigasi
                  antar-layar, dan dokumentasi pengumpulan.
Sistem kerja    : Koordinasi melalui WhatsApp dan kolaborasi Git
                  dengan pembagian satu branch per alur layar, lalu
                  digabung (merge) ke branch utama setelah diperiksa
                  bersama oleh seluruh anggota kelompok.

======================================================================
6. KONTRIBUSI SPESIFIK TIAP ANGGOTA
======================================================================
1. Lintang Balakosa Ardhana (126968):
   - Layar alur Wali & komponen umum:
     ui/screens/splash/SplashScreen.kt,
     ui/screens/onboarding/OnboardingScreen.kt,
     ui/screens/onboarding/OnboardingViewModel.kt,
     ui/screens/home/HomeScreen.kt,
     ui/screens/home/HomeViewModel.kt,
     ui/screens/riwayat/RiwayatScreen.kt,
     ui/screens/riwayat/RiwayatViewModel.kt,
     ui/screens/notifikasi/NotifikasiScreen.kt,
     ui/screens/profile/ProfileScreen.kt,
     ui/screens/profile/ProfileViewModel.kt
   - Custom Material 3 Theme & komponen bersama:
     ui/theme/Color.kt, ui/theme/Type.kt, ui/theme/Shape.kt,
     ui/theme/Theme.kt, ui/components/KetukDarkModeCard.kt,
     ui/components/KetukLogoutButton.kt, ui/components/StatusChip.kt
   - Animasi masuk bertingkat:
     ui/components/KetukAnimations.kt (FadeSlideIn & staggerDelay)

2. Nanda Valeri (127234):
   - Layar alur Autentikasi, Pilih Peran, dan Oma/Opa:
     ui/screens/login/LoginScreen.kt,
     ui/screens/login/LoginViewModel.kt,
     ui/screens/register/RegisterScreen.kt,
     ui/screens/register/RegisterViewModel.kt,
     ui/screens/verification/VerificationScreen.kt,
     ui/screens/verification/VerificationViewModel.kt,
     ui/screens/role/RoleSelectScreen.kt,
     ui/screens/role/RoleSelectViewModel.kt,
     ui/screens/oma/OmaScreens.kt (OmaPermissionsScreen &
     OmaPassiveScreen)
   - Dummy data & repositori simulasi autentikasi:
     data/dummy/DummyData.kt, data/dummy/DummyModels.kt,
     data/model/HomeSampleData.kt, data/model/RiwayatSampleData.kt,
     data/repository/AuthRepository.kt,
     data/repository/FakeAuthRepository.kt
   - Validasi form input & KeyboardType:
     ui/components/AuthComponents.kt, ui/components/KetukTextField.kt,
     ui/components/KetukButton.kt,
     ui/components/KetukSecondaryButton.kt

3. Paulus Marchionni Sadipun (126931):
   - Layar alur Penjaga, Setup Wali, Lokasi, Pindai QR, dan Detail:
     ui/screens/guardian/GuardianHomeScreen.kt,
     ui/screens/guardian/GuardianLokasiScreen.kt,
     ui/screens/guardian/GuardianLocationHistoryScreen.kt,
     ui/screens/guardian/GuardianProfileScreen.kt,
     ui/screens/guardian/GuardianViewModel.kt,
     ui/screens/walisetup/WaliSetupScreens.kt (DetailOmaOpaScreen,
     CircleKeluargaScreen, QrWaliScreen, PilihPenjagaScreen),
     ui/screens/lokasi/LokasiScreen.kt,
     ui/screens/lokasi/LokasiViewModel.kt,
     data/model/LokasiSampleData.kt,
     ui/screens/detail/DetailAlertScreen.kt,
     ui/screens/oma/OmaScreens.kt (ScanQrScreen &
     LoadingPairingOmaScreen)
   - Arsitektur Navigasi & pengiriman argumen dinamis:
     MainActivity.kt, navigation/KetukNavHost.kt, navigation/Routes.kt
   - Penyusunan README.txt dan pengecekan kelengkapan deliverable UTS
======================================================================
