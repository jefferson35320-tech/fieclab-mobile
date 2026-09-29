package com.example.myapplication.ui.theme.mvvm

import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ItemDao) {

    val allItems: Flow<List<Products>> = productDao.getAllItems()

    suspend fun getItemById(id: Long): Products? {
        return productDao.getItemById(id)
    }

    suspend fun insert(item: Products): Long {
        return productDao.insertItem(item)
    }

    suspend fun update(item: Products) {
        productDao.updateItem(item)
    }

    suspend fun delete(item: Products) {
        productDao.deleteItem(item)
    }
}
