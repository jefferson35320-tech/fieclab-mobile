package com.example.myapplication.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartItem(
    val product: Products,
    val quantity: Int = 1
)

data class ProductUiState(
    val products: List<Products> = emptyList(),
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val cartCount: Int get() = cartItems.sumOf { it.quantity }
    val cartTotalPrice: Double get() = cartItems.sumOf { it.product.price * it.quantity }
}

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository
    val authRepository: AuthRepository = AuthRepository()

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        val database = ProductDatabase.getDatabase(application)
        repository = ProductRepository(database.productDao())

        viewModelScope.launch {
            repository.allItems.collect { itemList ->
                if (itemList.isEmpty()) {
                    seedDefaultProducts()
                } else {
                    _uiState.update { currentState ->
                        currentState.copy(products = itemList)
                    }
                }
            }
        }

        syncWithBackend()
    }

    fun syncWithBackend() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.syncProductsFromBackend()
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.localizedMessage
                )
            }
        }
    }

    private suspend fun seedDefaultProducts() {
        val defaultProducts = listOf(
            Products(
                name = "Sabonete Artesanal de Glicerina",
                description = "Sabonete hidratante produzido com óleos essenciais e glicerina vegetal.",
                price = 12.50,
                chemicalFormula = "CABELO"
            ),
            Products(
                name = "Álcool em Gel Antisséptico 70%",
                description = "Formulação antisséptica com aloe vera para proteção das mãos.",
                price = 8.90,
                chemicalFormula = "PELE"
            ),
            Products(
                name = "Aromatizador de Ambientes Lavanda",
                description = "Spray aromatizante feito com extratos botânicos e solução alcoólica.",
                price = 22.00,
                chemicalFormula = "PERFUME"
            )
        )
        for (product in defaultProducts) {
            repository.insert(product)
        }
    }

    fun addToCart(product: Products) {
        _uiState.update { currentState ->
            val existingItemIndex = currentState.cartItems.indexOfFirst { it.product.id == product.id }
            val updatedList = currentState.cartItems.toMutableList()

            if (existingItemIndex != -1) {
                val currentItem = updatedList[existingItemIndex]
                updatedList[existingItemIndex] = currentItem.copy(quantity = currentItem.quantity + 1)
            } else {
                updatedList.add(CartItem(product = product, quantity = 1))
            }

            currentState.copy(cartItems = updatedList)
        }
    }

    fun removeFromCart(product: Products) {
        _uiState.update { currentState ->
            val existingItemIndex = currentState.cartItems.indexOfFirst { it.product.id == product.id }
            if (existingItemIndex == -1) return@update currentState

            val updatedList = currentState.cartItems.toMutableList()
            val currentItem = updatedList[existingItemIndex]

            if (currentItem.quantity > 1) {
                updatedList[existingItemIndex] = currentItem.copy(quantity = currentItem.quantity - 1)
            } else {
                updatedList.removeAt(existingItemIndex)
            }

            currentState.copy(cartItems = updatedList)
        }
    }

    fun deleteFromCart(product: Products) {
        _uiState.update { currentState ->
            val updatedList = currentState.cartItems.filterNot { it.product.id == product.id }
            currentState.copy(cartItems = updatedList)
        }
    }

    fun clearCart() {
        _uiState.update { currentState ->
            currentState.copy(cartItems = emptyList())
        }
    }
}
