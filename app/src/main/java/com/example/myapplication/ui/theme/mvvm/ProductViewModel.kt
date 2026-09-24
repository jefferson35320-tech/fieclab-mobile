package com.example.myapplication.ui.theme.mvvm

// ProductViewModel.kt
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProductUiState(
    val products: List<Product> = emptyList(),
    val cartCount: Int = 0,
    val isLoading: Boolean = false
)

class ProductViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        // Exemplo de produtos artesanais do curso de química
        val sampleProducts = listOf(
            Product("1", "Sabonete Artesanal de Lavanda", "Produzido via saponificação a frio", 12.50, "C17H35COOK"),
            Product("2", "Álcool em Gel 70% Aromático", "Formulação antiseptico com óleo essencial", 8.00, "C2H5OH"),
            Product("3", "Vela Aromática de Baunilha", "Cera vegetal com óleos essenciais", 25.00)
        )
        _uiState.value = ProductUiState(products = sampleProducts)
    }

    fun addToCart(product: Product) {
        val currentCount = _uiState.value.cartCount
        _uiState.value = _uiState.value.copy(cartCount = currentCount + 1)
    }
}