package com.chat.ketukprojectmap.ui.screens.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.ketukprojectmap.data.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VerificationUiState(
    val code: String = "",
    val resendSeconds: Int = 0,        // 0 = boleh kirim ulang
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null
) {
    val isCodeComplete: Boolean
        get() = code.length == VerificationViewModel.CODE_LENGTH
    val canResend: Boolean
        get() = resendSeconds == 0 && !isLoading
}

class VerificationViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerificationUiState())
    val uiState: StateFlow<VerificationUiState> = _uiState.asStateFlow()

    private var countdownJob: Job? = null

    init {
        // Layar ini dibuka tepat setelah kode dikirim, jadi cooldown langsung jalan.
        startCountdown()
    }

    /** Hanya menerima angka, maksimal CODE_LENGTH digit. */
    fun onCodeChange(newCode: String) {
        if (newCode.length <= CODE_LENGTH && newCode.all { it.isDigit() }) {
            _uiState.update {
                it.copy(code = newCode, errorMessage = null, infoMessage = null)
            }
        }
    }

    /** Verifikasi kode ke Supabase. onSuccess dipanggil kalau kode benar. */
    fun onConfirm(email: String, onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!state.isCodeComplete || state.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }
            try {
                repository.verifySignupCode(email, state.code)
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                android.util.Log.e("KetukVerify", "Verifikasi gagal", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        code = "",
                        errorMessage = mapError(e.message.orEmpty().lowercase())
                    )
                }
            }
        }
    }

    /** Kirim ulang kode, lalu tunggu RESEND_DELAY_SECONDS detik sebelum boleh lagi. */
    fun onResend(email: String) {
        if (!_uiState.value.canResend) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, code = "", errorMessage = null, infoMessage = null)
            }
            try {
                repository.resendSignupCode(email)
                _uiState.update {
                    it.copy(isLoading = false, infoMessage = "Kode baru sudah dikirim ke email kamu")
                }
                startCountdown()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                android.util.Log.e("KetukVerify", "Kirim ulang gagal", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = mapError(e.message.orEmpty().lowercase(), isResend = true)
                    )
                }
            }
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (second in RESEND_DELAY_SECONDS downTo 1) {
                _uiState.update { it.copy(resendSeconds = second) }
                delay(1000)
            }
            _uiState.update { it.copy(resendSeconds = 0) }
        }
    }

    private fun mapError(msg: String, isResend: Boolean = false): String = when {
        "network" in msg || "unable to resolve" in msg || "timeout" in msg ->
            "Tidak ada koneksi internet"
        "rate limit" in msg || "too many" in msg || "429" in msg || "after" in msg && "seconds" in msg ->
            "Terlalu sering meminta kode, tunggu sebentar lalu coba lagi"
        isResend -> "Gagal mengirim ulang kode, coba lagi"
        else -> "Kode salah atau sudah kedaluwarsa"
    }

    companion object {
        const val CODE_LENGTH: Int = 6
        const val RESEND_DELAY_SECONDS = 60
    }
}