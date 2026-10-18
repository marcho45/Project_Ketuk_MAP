package com.chat.ketukprojectmap.data.repository

import com.chat.ketukprojectmap.BuildConfig

/**
 * Kontrak autentikasi untuk aplikasi Ketuk.
 * Di mode `demo` (UTS) memakai [FakeAuthRepository] (in-memory offline).
 * Di mode `full` (UAS) memakai [SupabaseAuthRepository] (backend Supabase asli).
 */
interface AuthRepository {
    suspend fun login(email: String, password: String)

    /** Return true kalau akun langsung aktif (Confirm email mati). */
    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String
    ): Boolean

    /** Verifikasi kode OTP pendaftaran. */
    suspend fun verifySignupCode(email: String, code: String)

    /** Kirim ulang kode verifikasi pendaftaran. */
    suspend fun resendSignupCode(email: String)

    /** Cek apakah ada sesi login yang aktif. */
    suspend fun hasSession(): Boolean

    /** Role user yang sedang login ("wali", "oma_opa", "penjaga") atau null kalau belum dipilih. */
    suspend fun getRole(): String?

    /** Simpan role ("wali", "oma_opa", atau "penjaga"). */
    suspend fun updateRole(role: String)

    /** Keluar dari akun. */
    suspend fun signOut()

    /** Login dengan Google (dipakai langsung di mode demo; mode full memakai ComposeAuth). */
    suspend fun signInWithGoogle()
}

/**
 * Satu-satunya titik pemilihan implementasi [AuthRepository] berdasarkan [BuildConfig.DEMO_MODE].
 */
fun AuthRepository(): AuthRepository =
    if (BuildConfig.DEMO_MODE) FakeAuthRepository else SupabaseAuthRepository()