package com.example.myapplication.ui.theme.mvvm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items ORDER BY timestamp DESC")
    fun getAllItems(): Flow<List<Products>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemById(id: Long): Products?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Products): Long

    @Update
    suspend fun updateItem(item: Products)

    @Delete
    suspend fun deleteItem(item: Products)

    @Query("DELETE FROM items")
    suspend fun deleteAll()
}