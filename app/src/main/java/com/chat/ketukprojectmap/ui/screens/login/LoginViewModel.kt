package com.chat.ketukprojectmap.ui.screens.login



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.ketukprojectmap.data.repository.AuthRepository
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginState(
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val error: String? = null
)

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun clearError() {
        if (_state.value.error != null || _state.value.emailError != null || _state.value.passwordError != null) {
            _state.update { it.copy(error = null, emailError = null, passwordError = null) }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onNeedVerification: () -> Unit
    ) {
        val trimmedEmail = email.trim()
        val emailErr = when {
            trimmedEmail.isBlank() -> "Email tidak boleh kosong"
            !isValidEmail(trimmedEmail) -> "Format email tidak valid"
            else -> null
        }
        val passErr = when {
            password.isBlank() -> "Password tidak boleh kosong"
            password.length < 6 -> "Password minimal 6 karakter"
            else -> null
        }
        if (emailErr != null || passErr != null) {
            _state.update {
                it.copy(
                    emailError = emailErr,
                    passwordError = passErr,
                    error = null
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { LoginState(isLoading = true) }
            try {
                repository.login(trimmedEmail, password)
                _state.update { LoginState() }
                onSuccess()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val msg = e.message.orEmpty().lowercase()
                if ("not confirmed" in msg || "email_not_confirmed" in msg) {
                    _state.update { LoginState() }
                    onNeedVerification()
                } else {
                    _state.update { LoginState(error = mapError(msg)) }
                }
            }
        }
    }

    fun signInWithGoogleDemo(onSuccess: () -> Unit) {
        if (_state.value.isLoading) return
        viewModelScope.launch {
            _state.update { LoginState(isLoading = true) }
            try {
                repository.signInWithGoogle()
                _state.update { LoginState() }
                onSuccess()
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _state.update { LoginState(error = "Login Google gagal") }
            }
        }
    }

    fun onGoogleResult(result: NativeSignInResult, onSuccess: () -> Unit) {
        when (result) {
            is NativeSignInResult.Success -> onSuccess()
            is NativeSignInResult.ClosedByUser -> Unit
            is NativeSignInResult.NetworkError ->
                _state.update { it.copy(error = "Tidak ada koneksi internet") }
            is NativeSignInResult.Error ->
                _state.update { it.copy(error = "Login Google gagal") }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    private fun mapError(msg: String): String = when {
        "invalid login credentials" in msg || "invalid_credentials" in msg ->
            "Email/Nomor HP atau password salah"
        "network" in msg || "unable to resolve" in msg || "timeout" in msg ->
            "Tidak ada koneksi internet"
        else -> "Gagal masuk, coba lagi"
    }
}