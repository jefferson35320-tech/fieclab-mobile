package com.example.myapplication.ui.theme.mvvm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Products(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)