package com.example.myapplication.mvvm

import com.example.myapplication.data.remote.FiecLabApi
import com.example.myapplication.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val productDao: ProductDao,
    private val api: FiecLabApi = RetrofitClient.api,
) {

    val allItems: Flow<List<Products>> = productDao.getAllItems()

    suspend fun syncProductsFromBackend(): Result<Unit> {
        return try {
            val response = api.getProducts(page = 0, size = 100)
            if (response.isSuccessful && response.body() != null) {
                val remoteList = response.body()?.content ?: emptyList()
                if (remoteList.isNotEmpty()) {
                    for (dto in remoteList) {
                        val existing = productDao.getItemByName(dto.name)
                        val entity = Products(
                            id = existing?.id ?: 0,
                            name = dto.name,
                            description = dto.description ?: "Produto artesanal do laboratório FIEC",
                            price = dto.price ?: 0.0,
                            chemicalFormula = dto.type ?: "Fórmula FIEC",
                        )
                        productDao.insertItem(entity)
                    }
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro na API do backend (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
