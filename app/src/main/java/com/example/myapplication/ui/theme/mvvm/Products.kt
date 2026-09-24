package com.example.myapplication.ui.theme.mvvm

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val chemicalFormula: String? = null
)
