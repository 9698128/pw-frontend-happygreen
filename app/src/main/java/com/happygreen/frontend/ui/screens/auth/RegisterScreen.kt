package com.happygreen.frontend.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // Naviga alla home se la registrazione ha successo
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header con pulsante indietro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateToLogin) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        // Logo/Titolo
        Text(
            text = "🌱 HappyGreen",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Crea il tuo account",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo Nome
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome completo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nome") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Campo Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Campo Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Nascondi password"
                        else "Mostra password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Campo Conferma Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Conferma password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Conferma password") },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Nascondi password"
                        else "Mostra password"
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = confirmPassword.isNotEmpty() && password != confirmPassword,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Messaggio di errore per password non corrispondenti
        if (confirmPassword.isNotEmpty() && password != confirmPassword) {
            Text(
                text = "Le password non corrispondono",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Pulsante Registrazione
        Button(
            onClick = {
                viewModel.clearMessages()
                viewModel.register(name.trim(), email.trim(), password, confirmPassword)
            },
            enabled = !uiState.isLoading &&
                    name.isNotBlank() &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    confirmPassword.isNotBlank() &&
                    password == confirmPassword,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Registrati", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Link login
        TextButton(onClick = onNavigateToLogin) {
            Text("Hai già un account? Accedi")
        }

        // Messaggi di errore/successo
        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        uiState.successMessage?.let { success ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = success,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}