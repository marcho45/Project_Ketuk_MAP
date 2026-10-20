package com.chat.ketukprojectmap.data.repository

import com.chat.ketukprojectmap.data.remote.SupabaseProvider
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
private data class ProfileRole(val role: String? = null)

/**
 * Implementasi autentikasi Supabase asli untuk Build Variant `full` (UAS).
 * Membungkus seluruh kode Supabase Auth, OTP, dan profil tanpa mengubah perilakunya.
 */
class SupabaseAuthRepository : AuthRepository {
    private val auth get() = SupabaseProvider.client.auth

    override suspend fun login(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email.trim()
            this.password = password
        }
    }

    /** Return true kalau akun langsung aktif (Confirm email mati). */
    override suspend fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String
    ): Boolean {
        auth.signUpWith(Email) {
            this.email = email.trim()
            this.password = password
            data = buildJsonObject {
                put("full_name", fullName.trim())
                put("phone", phone)
            }
        }
        return auth.currentSessionOrNull() != null
    }

    /**
     * Verifikasi kode OTP yang dikirim ke email saat daftar.
     * Kalau berhasil, sesi login otomatis tersimpan.
     */
    override suspend fun verifySignupCode(email: String, code: String) {
        auth.verifyEmailOtp(
            type = OtpType.Email.SIGNUP,
            email = email.trim(),
            token = code
        )
    }

    /** Kirim ulang kode verifikasi pendaftaran. */
    override suspend fun resendSignupCode(email: String) {
        auth.resendEmail(OtpType.Email.SIGNUP, email.trim())
    }

    /** Dipanggil saat app dibuka: tunggu sesi tersimpan selesai dimuat, lalu cek. */
    override suspend fun hasSession(): Boolean {
        auth.awaitInitialization()
        return auth.currentSessionOrNull() != null
    }

    /** Role user yang sedang login ("wali", "oma_opa", "penjaga") atau null kalau belum dipilih. */
    override suspend fun getRole(): String? {
        val userId = auth.currentUserOrNull()?.id ?: return null
        return SupabaseProvider.client.from("profiles")
            .select(columns = Columns.list("role")) {
                filter { eq("id", userId) }
            }
            .decodeSingleOrNull<ProfileRole>()
            ?.role
    }

    /** Simpan role. Nilai harus persis: "wali", "oma_opa", atau "penjaga". */
    override suspend fun updateRole(role: String) {
        val userId = auth.currentUserOrNull()?.id ?: error("Belum login")
        SupabaseProvider.client.from("profiles").update({
            set("role", role)
        }) {
            filter { eq("id", userId) }
        }
    }

    /** Keluar dari akun. Jika gagal (offline), tetap hapus sesi lokal. */
    override suspend fun signOut() {
        try {
            auth.signOut()
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            // Jika gagal (offline), paksa logout lokal
            try {
                auth.signOut()
            } catch (_: Exception) {
                // Ignore, minimal sudah coba
            }
        }
    }

    override suspend fun signInWithGoogle() {
        // Di variant `full`, Google Sign-In dijalankan via Supabase composeAuth.rememberSignInWithGoogle di UI.
    }
}
