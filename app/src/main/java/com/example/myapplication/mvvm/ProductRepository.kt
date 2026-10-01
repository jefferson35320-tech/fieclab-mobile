package com.example.myapplication.mvvm

import com.example.myapplication.data.remote.FiecLabApi
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.data.remote.dto.ProductDto
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
                    val localEntities = remoteList.map { dto -> dto.toEntity() }
                    for (entity in localEntities) {
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

    private fun ProductDto.toEntity(): Products {
        return Products(
            name = this.name,
            description = this.description ?: "Produto artesanal do laboratório FIEC",
            price = this.price,
            chemicalFormula = this.type ?: "Fórmula FIEC",
        )
    }
}
