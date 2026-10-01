package com.example.myapplication.data.remote.dto

data class ProductDto(
    val id: String? = null,
    val name: String,
    val price: Double,
    val description: String? = null,
    val imageUrl: String? = null,
    val type: String? = null,
    val batch: Int? = null,
    val mfgDate: String? = null,
    val expDate: String? = null,
    val supplierId: Int? = null
)
