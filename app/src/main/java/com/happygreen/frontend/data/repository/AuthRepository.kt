package com.happygreen.frontend.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.happygreen.frontend.data.api.AuthApiService
import com.happygreen.frontend.data.model.AuthResponse
import com.happygreen.frontend.data.model.ErrorResponse
import com.happygreen.frontend.data.model.LoginRequest
import com.happygreen.frontend.data.model.RegisterRequest
import com.happygreen.frontend.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApiService: AuthApiService,
    private val gson: Gson
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_KEY = stringPreferencesKey("user_data")
    }

    // Flow per osservare lo stato di autenticazione
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY] != null
    }

    val currentUser: Flow<User?> = context.dataStore.data.map { preferences ->
        preferences[USER_KEY]?.let { userJson ->
            try {
                gson.fromJson(userJson, User::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    // Salva i dati di autenticazione
    private suspend fun saveAuthData(token: String, user: User) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = "Bearer $token"
            preferences[USER_KEY] = gson.toJson(user)
        }
    }

    // Rimuove i dati di autenticazione
    suspend fun clearAuthData() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(USER_KEY)
        }
    }

    // Ottiene il token corrente
    suspend fun getToken(): String? {
        return context.dataStore.data.first()[TOKEN_KEY]
    }

    // Login
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = authApiService.login(LoginRequest(email, password))
            handleAuthResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Registrazione
    suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Result<AuthResponse> {
        return try {
            val response = authApiService.register(
                RegisterRequest(name, email, password, passwordConfirmation)
            )
            handleAuthResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Logout
    suspend fun logout(): Result<Unit> {
        return try {
            val token = getToken()
            if (token != null) {
                authApiService.logout(token)
            }
            clearAuthData()
            Result.success(Unit)
        } catch (e: Exception) {
            clearAuthData() // Pulisce comunque i dati locali
            Result.success(Unit)
        }
    }

    // Gestisce le risposte di autenticazione
    private suspend fun handleAuthResponse(response: Response<AuthResponse>): Result<AuthResponse> {
        return if (response.isSuccessful) {
            val authResponse = response.body()
            if (authResponse?.success == true && authResponse.data != null) {
                saveAuthData(authResponse.data.token, authResponse.data.user)
                Result.success(authResponse)
            } else {
                Result.failure(Exception(authResponse?.message ?: "Errore sconosciuto"))
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = try {
                gson.fromJson(errorBody, ErrorResponse::class.java)
            } catch (e: Exception) {
                ErrorResponse(false, "Errore di connessione")
            }
            Result.failure(Exception(errorResponse.message))
        }
    }
}