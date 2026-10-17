package com.chat.ketukprojectmap.data.repository

import com.chat.ketukprojectmap.data.remote.SupabaseProvider
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

class AuthRepository {
    private val auth get() = SupabaseProvider.client.auth

    suspend fun login(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email.trim()
            this.password = password
        }
    }

    /** Return true kalau akun langsung aktif (Confirm email mati). */
    suspend fun register(
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

    /** Dipanggil saat app dibuka: tunggu sesi tersimpan selesai dimuat, lalu cek. */
    suspend fun hasSession(): Boolean {
        auth.awaitInitialization()
        return auth.currentSessionOrNull() != null
    }

    /** Role user yang sedang login ("wali", "oma_opa", "penjaga") atau null kalau belum dipilih. */
    suspend fun getRole(): String? {
        val userId = auth.currentUserOrNull()?.id ?: return null
        return SupabaseProvider.client.from("profiles")
            .select(columns = Columns.list("role")) {
                filter { eq("id", userId) }
            }
            .decodeSingleOrNull<ProfileRole>()
            ?.role
    }

    /** Simpan role. Nilai harus persis: "wali", "oma_opa", atau "penjaga". */
    suspend fun updateRole(role: String) {
        val userId = auth.currentUserOrNull()?.id ?: error("Belum login")
        SupabaseProvider.client.from("profiles").update({
            set("role", role)
        }) {
            filter { eq("id", userId) }
        }
    }

    /** Keluar dari akun. Jika gagal (offline), tetap hapus sesi lokal. */
    suspend fun signOut() {
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
}