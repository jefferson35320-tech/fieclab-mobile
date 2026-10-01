package com.example.myapplication.mvvm

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.myapplication.R
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.ui.theme.ChemDarkTeal
import com.example.myapplication.ui.theme.ChemTeal
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    val webClientId = stringResource(id = R.string.default_web_client_id)
    val authRepository = remember { AuthRepository() }

    fun handleEmailLogin() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Preencha o e-mail e a senha."
            return
        }

        coroutineScope.launch {
            isLoading = true
            errorMessage = null
            val result = authRepository.login(email, password)

            result.onSuccess {
                Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }.onFailure { exception ->
                Log.e("LoginScreen", "Erro no login backend", exception)
                errorMessage = "Erro ao conectar com servidor: ${exception.localizedMessage}"
            }
            isLoading = false
        }
    }

    fun handleGoogleSignIn() {
        if (webClientId.contains("YOUR_WEB_CLIENT_ID")) {
            Toast.makeText(
                context,
                "Configure seu Web Client ID em res/values/strings.xml para habilitar o Google Sign-In",
                Toast.LENGTH_LONG,
            ).show()
        }

        coroutineScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(webClientId)
                    .setAutoSelectEnabled(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )

                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val userEmail = googleIdTokenCredential.id
                    val displayName = googleIdTokenCredential.displayName ?: userEmail

                    Toast.makeText(context, "Bem-vindo(a), $displayName!", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                } else {
                    errorMessage = "Tipo de credencial não suportado."
                }
            } catch (_: GetCredentialCancellationException) {
                Log.d("LoginScreen", "Login com Google cancelado pelo usuário.")
            } catch (e: GetCredentialException) {
                Log.e("LoginScreen", "Erro no login com Google", e)
                errorMessage = "Erro ao autenticar com Google: ${e.localizedMessage}"
            } catch (e: GoogleIdTokenParsingException) {
                Log.e("LoginScreen", "Erro ao processar token do Google", e)
                errorMessage = "Token do Google inválido."
            } finally {
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(ChemTeal, ChemDarkTeal),
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = "Logo Química",
                    tint = ChemTeal,
                    modifier = Modifier.size(64.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "FIEC-LAB",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ChemDarkTeal,
                    ),
                )
                Text(
                    text = "Produtos Artesanais dos Alunos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail do Aluno/Cliente") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { handleEmailLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ChemTeal),
                    enabled = !isLoading,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text("ENTRAR", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        text = "  OU  ",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { handleGoogleSignIn() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                    ),
                    border = BorderStroke(1.dp, Color(0xFFDADCE0)),
                    enabled = !isLoading,
                ) {
                    Text(
                        text = "G",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = Color(0xFF4285F4),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Entrar com o Google",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3C4043),
                    )
                }
            }
        }
    }
}
