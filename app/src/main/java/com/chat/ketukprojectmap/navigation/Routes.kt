package com.chat.ketukprojectmap.navigation

import android.net.Uri

object Routes {
    const val splash = "splash"
    const val onboarding = "onboarding"
    const val login = "login"
    const val register = "register"
    const val verification = "verification" // tanpa email (dipakai Login)
    const val role = "role"
    const val home = "home"
    const val notifikasi = "notifikasi"

    // Route Setup Wali (02. Initial Phase Wali)
    const val detailOmaOpa = "detail_oma_opa"
    const val circleKeluarga = "circle_keluarga"
    const val qrWali = "qr_wali"
    const val pilihPenjaga = "pilih_penjaga"

    // Route Detail Alert / Aktivitas (M8 - 04. Core Phase Wali)
    const val detailAlertPattern = "detail_alert/{itemId}"
    fun detailAlert(itemId: String) = "detail_alert/${Uri.encode(itemId)}"

    // Pola untuk NavHost; email bersifat opsional
    const val verificationPattern = "verification?email={email}"

    // Dipakai Register: membawa email ke layar verifikasi
    fun verificationWithEmail(email: String) =
        "verification?email=${Uri.encode(email)}"

    // Helper terpusat untuk menentukan route beranda berdasarkan peran
    fun homeRouteForRole(role: String?): String = when (role) {
        "wali" -> home
        else -> home
    }

    // Helper terpusat untuk menentukan route setelah memilih peran di RoleSelectScreen
    fun setupRouteForRole(role: String?): String = when (role) {
        "wali" -> detailOmaOpa
        else -> homeRouteForRole(role)
    }
}