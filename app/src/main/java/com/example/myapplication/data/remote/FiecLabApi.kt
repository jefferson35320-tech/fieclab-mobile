package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.AuthLoginRequest
import com.example.myapplication.data.remote.dto.AuthRegisterRequest
import com.example.myapplication.data.remote.dto.AuthResponse
import com.example.myapplication.data.remote.dto.ProductDto
import com.example.myapplication.data.remote.dto.SpringPageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FiecLabApi {

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: AuthLoginRequest): Response<AuthResponse>

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: AuthRegisterRequest): Response<Void>

    @GET("api/products")
    suspend fun getProducts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 50
    ): Response<SpringPageResponse<ProductDto>>

    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ProductDto>

    @POST("api/products")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body product: ProductDto
    ): Response<ProductDto>
}
