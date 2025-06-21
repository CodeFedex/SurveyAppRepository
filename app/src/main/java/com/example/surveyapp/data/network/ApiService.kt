package com.example.surveyapp.data.network

import com.example.surveyapp.data.model.LoginRequest
import com.example.surveyapp.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST



interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

}