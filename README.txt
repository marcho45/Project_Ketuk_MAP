==================================================================
1. README - KETUK

2. ANGGOTA KELOMPOK
--------------------
1. Lintang Balakosa Ardhana (126968)
2. Nanda Valeri (127234)
3. Paulus Marchionni Sadipun (126931)

3. PEMBAGIAN TUGAS DAN SISTEM KERJA
------------------------------------
Pembagian tugas : Dibagi rata berdasarkan alur layar. Lintang
                  memegang alur Wali (pemantauan dan riwayat),
                  Nanda memegang alur Autentikasi dan Oma/Opa,
                  Paulus memegang alur Penjaga serta layar
                  lokasi dan detail Oma/Opa. Tugas lintas-layar
                  (theme, navigasi, animasi, dummy data,
                  validasi, dokumentasi) juga dibagi rata
                  (lihat bagian 4).
Sistem kerja    : Koordinasi lewat WhatsApp dan Git dengan
                  pembagian branch per alur layar, lalu digabung
                  ke branch utama.
Kesepakatan     : theme (Color, Type, Shape), komponen bersama di
                  ui/components, dan dokumentasi di folder docs/
                  dipakai bersama oleh semua anggota.

4. KONTRIBUSI SPESIFIK TIAP ANGGOTA
-----------------------------------
Lintang Balakosa Ardhana (126968):
  - Layar alur Wali: HomeScreen, RiwayatScreen, NotifikasiScreen,
    DetailAlertScreen, ProfileScreen
  - Custom Material 3 theme: Color.kt, Type.kt, Shape.kt, Theme.kt
  - Animasi masuk bertingkat: KetukAnimations.kt

Nanda Valeri (127234):
  - Layar alur Autentikasi dan Oma/Opa: LoginScreen,
    RegisterScreen, VerificationScreen, OmaPermissionsScreen,
    OmaPassiveScreen
  - Dummy data: DummyData.kt, DummyModels.kt
  - Validasi form dan KeyboardType (email, nomor HP, umur, field
    wajib)

Paulus Marchionni Sadipun (126931):
  - Layar alur Penjaga, lokasi, dan detail: GuardianHomeScreen,
    GuardianLokasiScreen, GuardianLocationHistoryScreen,
    DetailOmaOpaScreen, LokasiScreen, ScanQrScreen
  - Navigation: KetukNavHost.kt, Routes.kt (argumen
    detail_alert/{itemId} dan verification?email={email})
  - Dokumentasi di folder docs/ dan README