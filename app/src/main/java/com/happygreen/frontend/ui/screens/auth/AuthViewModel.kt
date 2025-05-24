package com.happygreen.frontend.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happygreen.frontend.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        // Osserva lo stato di login
        viewModelScope.launch {
            authRepository.isLoggedIn.collect { isLoggedIn ->
                _uiState.value = _uiState.value.copy(isLoggedIn = isLoggedIn)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            authRepository.login(email, password)
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = response.message
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Errore durante il login"
                    )
                }
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            // Validazione locale
            if (password != confirmPassword) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Le password non corrispondono"
                )
                return@launch
            }

            authRepository.register(name, email, password, confirmPassword)
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = response.message
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Errore durante la registrazione"
                    )
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}