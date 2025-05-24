package com.happygreen.frontend.data.model

import com.google.gson.annotations.SerializedName

// Richieste
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class RegisterRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String
)

// Risposte
data class AuthResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: AuthData? = null
)

data class AuthData(
    @SerializedName("user")
    val user: User,
    @SerializedName("token")
    val token: String
)

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

// Errori
data class ErrorResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("errors")
    val errors: Map<String, List<String>>? = null
)