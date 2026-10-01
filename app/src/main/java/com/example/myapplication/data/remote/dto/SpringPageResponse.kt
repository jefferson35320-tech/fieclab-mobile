package com.example.myapplication.data.remote.dto

data class SpringPageResponse<T>(
    val content: List<T> = emptyList(),
    val totalPages: Int? = null,
    val totalElements: Long? = null,
    val number: Int? = null
)
