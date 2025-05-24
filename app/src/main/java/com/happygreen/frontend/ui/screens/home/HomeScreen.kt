package com.happygreen.frontend.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.happygreen.frontend.ui.screens.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by authViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Header con titolo app
        Text(
            text = "🌱 HappyGreen",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Benvenuto nella tua app green!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Card con informazioni
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎯 Prossimi sviluppi:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val features = listOf(
                    "📊 Dashboard personalizzata",
                    "🌿 Tracciamento abitudini eco-friendly",
                    "🏆 Sistema di sfide e ricompense",
                    "👥 Community green",
                    "📈 Analisi impatto ambientale"
                )

                features.forEach { feature ->
                    Text(
                        text = feature,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }

        // Pulsante Logout
        Button(
            onClick = {
                authViewModel.logout()
                onLogout()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.ExitToApp,
                contentDescription = "Logout",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Esci")
        }
    }
}