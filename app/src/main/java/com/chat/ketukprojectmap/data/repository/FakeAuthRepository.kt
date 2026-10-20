package com.chat.ketukprojectmap.data.repository

import com.chat.ketukprojectmap.data.dummy.DummyData
import kotlinx.coroutines.delay

/**
 * Implementasi autentikasi in-memory untuk Build Variant `demo` (UTS).
 * Berjalan 100% offline tanpa menyentuh Supabase atau jaringan.
 */
object FakeAuthRepository : AuthRepository {

    private const val DEMO_DELAY_MS = 800L

    private data class RegisteredDemoUser(
        val email: String,
        val password: String,
        val fullName: String,
        val phone: String,
        var isVerified: Boolean = false,
        var roleDbValue: String? = null
    )

    // Penyimpanan in-memory selama aplikasi berjalan
    private val registeredUsers = mutableMapOf<String, RegisteredDemoUser>()

    var currentEmail: String? = null
        private set

    var currentFullName: String? = null
        private set

    private var currentRoleDbValue: String? = null
    private var isLoggedIn: Boolean = false

    override suspend fun login(email: String, password: String) {
        delay(DEMO_DELAY_MS)
        val trimmed = email.trim().lowercase()

        // 1. Cek 3 akun demo resmi (wali@ketuk.app, oma@ketuk.app, penjaga@ketuk.app)
        val presetAccount = DummyData.demoAccounts.find {
            it.email.equals(trimmed, ignoreCase = true) || it.phone == trimmed
        }
        if (presetAccount != null) {
            if (presetAccount.password != password) {
                throw IllegalArgumentException("invalid login credentials: Email/Nomor HP atau password salah")
            }
            isLoggedIn = true
            currentEmail = presetAccount.email
            currentFullName = presetAccount.fullName
            currentRoleDbValue = presetAccount.roleDbValue
            return
        }

        // 2. Cek akun yang baru selesai didaftarkan lewat alur Register di sesi demo ini
        val customUser = registeredUsers[trimmed]
        if (customUser != null && customUser.password == password) {
            isLoggedIn = true
            currentEmail = customUser.email
            currentFullName = customUser.fullName
            currentRoleDbValue = customUser.roleDbValue
            return
        }

        throw IllegalArgumentException("invalid login credentials: Email/Nomor HP atau password salah")
    }

    override suspend fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String
    ): Boolean {
        delay(DEMO_DELAY_MS)
        val trimmed = email.trim().lowercase()
        registeredUsers[trimmed] = RegisteredDemoUser(
            email = email.trim(),
            password = password,
            fullName = fullName.trim(),
            phone = phone.trim(),
            isVerified = false,
            roleDbValue = null
        )
        // Return false agar alur demo masuk ke layar OTP (4 digit) -> Pilih Peran
        return false
    }

    override suspend fun verifySignupCode(email: String, code: String) {
        delay(DEMO_DELAY_MS)
        if (code.length != 4 || !code.all { it.isDigit() }) {
            throw IllegalArgumentException("Kode OTP harus 4 digit angka")
        }
        val trimmed = email.trim().lowercase()
        val user = registeredUsers[trimmed]
        if (user != null) {
            user.isVerified = true
            currentEmail = user.email
            currentFullName = user.fullName
            currentRoleDbValue = user.roleDbValue
        } else {
            currentEmail = email.trim().ifEmpty { "pengguna.baru@ketuk.app" }
            currentFullName = DummyData.defaultWaliName
            currentRoleDbValue = null
        }
        isLoggedIn = true
    }

    override suspend fun resendSignupCode(email: String) {
        delay(DEMO_DELAY_MS)
    }

    override suspend fun hasSession(): Boolean {
        return isLoggedIn
    }

    override suspend fun getRole(): String? {
        return if (isLoggedIn) currentRoleDbValue else null
    }

    override suspend fun updateRole(role: String) {
        delay(DEMO_DELAY_MS)
        currentRoleDbValue = role
        currentEmail?.lowercase()?.let { key ->
            registeredUsers[key]?.roleDbValue = role
        }
        isLoggedIn = true
    }

    override suspend fun signOut() {
        delay(300L)
        isLoggedIn = false
        currentEmail = null
        currentFullName = null
        currentRoleDbValue = null
    }

    override suspend fun signInWithGoogle() {
        delay(DEMO_DELAY_MS)
        isLoggedIn = true
        currentEmail = "google.demo@ketuk.app"
        currentFullName = DummyData.defaultWaliName
        // Belum punya role agar langsung diarahkan ke layar Pilih Peran ("Siapa kamu?")
        currentRoleDbValue = null
    }
}
