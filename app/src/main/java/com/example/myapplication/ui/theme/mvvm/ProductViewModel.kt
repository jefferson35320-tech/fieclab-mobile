package com.example.myapplication.ui.theme.mvvm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Product(
    val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val chemicalFormula: String? = null
)

data class ProductUiState(
    val products: List<Product> = emptyList(),
    val cartCount: Int = 0
)

class ProductViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProductUiState(
            products = listOf(
                Product(
                    id = 1,
                    name = "Sabonete Artesanal de Glicerina",
                    description = "Sabonete hidratante produzido com óleos essenciais e glicerina vegetal.",
                    price = 12.50,
                    chemicalFormula = "C3H8O3"
                ),
                Product(
                    id = 2,
                    name = "Álcool em Gel Antisséptico 70%",
                    description = "Formulação antisséptica com aloe vera para proteção das mãos.",
                    price = 8.90,
                    chemicalFormula = "C2H6O"
                ),
                Product(
                    id = 3,
                    name = "Aromatizador de Ambientes Lavanda",
                    description = "Spray aromatizante feito com extratos botânicos e solução alcoólica.",
                    price = 22.00,
                    chemicalFormula = "C10H18O"
                ),
                Product(
                    id = 4,
                    name = "Detergente Ecológico Biodegradável",
                    description = "Detergente neutro de alta eficiência e baixo impacto ambiental.",
                    price = 6.50,
                    chemicalFormula = "C12H25SO4Na"
                )
            )
        )
    )
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    fun addToCart(product: Product) {
        _uiState.update { currentState ->
            currentState.copy(cartCount = currentState.cartCount + 1)
        }
    }
}
