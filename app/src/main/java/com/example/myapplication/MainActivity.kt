package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.QuimicaStoreTheme
import com.example.myapplication.ui.theme.mvvm.LoginScreen
import com.example.myapplication.ui.theme.mvvm.ShopScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuimicaStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login",
    ) {
        composable("login") {
            LoginScreen {
                navController.navigate("shop") {
                    // Remove a tela de login da pilha para o usuário não voltar a ela apertando "voltar"
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("shop") {
            ShopScreen()
        }
    }
}
