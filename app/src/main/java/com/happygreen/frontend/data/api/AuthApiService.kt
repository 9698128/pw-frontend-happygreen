package com.happygreen.frontend.data.api

import com.happygreen.frontend.data.model.AuthResponse
import com.happygreen.frontend.data.model.LoginRequest
import com.happygreen.frontend.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<AuthResponse>

    @POST("auth/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<AuthResponse>
}