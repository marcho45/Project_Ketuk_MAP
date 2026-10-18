package com.chat.ketukprojectmap.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.ketukprojectmap.data.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullNameError: String = "",
    val emailError: String = "",
    val phoneNumberError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",
    val error: String = "",
    val isLoading: Boolean = false
)

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value, fullNameError = "", error = "") }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = "", error = "") }
    }

    fun onPhoneNumberChange(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.update { it.copy(phoneNumber = filtered, phoneNumberError = "", error = "") }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = "", error = "") }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = "", error = "") }
    }

    // Dipanggil saat login Google gagal; pesannya tampil di bawah form
    fun onGoogleError(message: String) {
        _uiState.update { it.copy(error = message, isLoading = false) }
    }

    fun signInWithGoogleDemo(onSuccess: () -> Unit) {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }
            try {
                repository.signInWithGoogle()
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Login Google gagal") }
            }
        }
    }

    fun register(
        onSuccess: () -> Unit,
        onNeedVerification: () -> Unit = onSuccess
    ) {
        val state = _uiState.value
        var hasError = false

        if (state.fullName.isBlank()) {
            _uiState.update { it.copy(fullNameError = "Nama lengkap tidak boleh kosong") }
            hasError = true
        }

        if (state.email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email tidak boleh kosong") }
            hasError = true
        } else if (!isValidEmail(state.email)) {
            _uiState.update { it.copy(emailError = "Format email tidak valid") }
            hasError = true
        }

        if (state.phoneNumber.isBlank()) {
            _uiState.update { it.copy(phoneNumberError = "Nomor HP tidak boleh kosong") }
            hasError = true
        } else if (state.phoneNumber.length < 9 || state.phoneNumber.length > 13) {
            _uiState.update { it.copy(phoneNumberError = "Nomor HP harus 9-13 digit") }
            hasError = true
        }

        if (state.password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Password tidak boleh kosong") }
            hasError = true
        } else if (state.password.length < 8) {
            _uiState.update { it.copy(passwordError = "Password minimal 8 karakter") }
            hasError = true
        }

        if (state.confirmPassword.isBlank()) {
            _uiState.update { it.copy(confirmPasswordError = "Konfirmasi password tidak boleh kosong") }
            hasError = true
        } else if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Password tidak sama") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }
            try {
                val loggedIn = repository.register(
                    email = state.email,
                    password = state.password,
                    fullName = state.fullName,
                    phone = state.phoneNumber
                )
                _uiState.update { it.copy(isLoading = false) }
                if (loggedIn) onSuccess() else onNeedVerification()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                android.util.Log.e("KetukRegister", "Register gagal", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = mapError(e.message.orEmpty().lowercase()) +
                                " [" + e.message.orEmpty().take(120) + "]"
                    )
                }
            }
        }
    }

    private fun mapError(msg: String): String = when {
        "already registered" in msg -> "Email sudah terdaftar"
        "database error" in msg -> "Nomor HP sudah terdaftar"
        "network" in msg || "unable to resolve" in msg || "timeout" in msg ->
            "Tidak ada koneksi internet"
        "password" in msg -> "Password terlalu lemah"
        else -> "Pendaftaran gagal, coba lagi"
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    fun isFormValid(): Boolean {
        val state = _uiState.value
        return state.fullName.isNotBlank() &&
                state.email.isNotBlank() &&
                state.phoneNumber.isNotBlank() &&
                state.password.isNotBlank() &&
                state.confirmPassword.isNotBlank()
    }
}