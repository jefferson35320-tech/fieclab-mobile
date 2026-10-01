package com.example.myapplication.data.repository

import com.example.myapplication.data.remote.FiecLabApi
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.data.remote.dto.AuthLoginRequest
import com.example.myapplication.data.remote.dto.AuthRegisterRequest

class AuthRepository(private val api: FiecLabApi = RetrofitClient.api) {

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = api.login(AuthLoginRequest(email = email, password = password))
            if (response.isSuccessful) {
                val token = response.body()?.token ?: ""
                Result.success(token)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Falha no login (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            val response = api.register(AuthRegisterRequest(name = name, email = email, password = password))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Falha no cadastro (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
