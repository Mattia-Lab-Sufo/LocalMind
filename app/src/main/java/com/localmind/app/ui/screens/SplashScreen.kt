package com.localmind.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localmind.app.R

@Composable
fun SplashScreen(onInitializationComplete: () -> Unit) {
    // Stati di caricamento per replicare i passaggi fluidi
    var dbInit by remember { mutableStateOf(true) }
    var downloadCheck by remember { mutableStateOf(true) }
    var hardwareRead by remember { mutableStateOf(true) }
    var catalogLoad by remember { mutableStateOf(true) }
    var subInit by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // Pausa breve per mostrare la schermata e procedere alla chat
        kotlinx.coroutines.delay(2500)
        onInitializationComplete()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0D1117) // Sfondo scuro e moderno in stile Material 3
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Immagine della CPU con la scritta AI in 3D
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_app),
                    contentDescription = "AI CPU Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "LocalMind",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Preparazione dell'area AI",
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(40.dp))

            // Lista dei controlli di sistema con spunte pulite
            InitStepItem(text = "Inizializzazione database", isDone = dbInit)
            InitStepItem(text = "Controllo download", isDone = downloadCheck)
            InitStepItem(text = "Caricamento modelli sul dispositivo", isDone = hardwareRead)
            InitStepItem(text = "Lettura informazioni hardware", isDone = catalogLoad)
            InitStepItem(text = "Recupero catalogo modelli", isDone = subInit)
        }
    }
}

@Composable
fun InitStepItem(text: String, isDone: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.LightGray
        )
        Text(
            text = if (isDone) "✓" else "⏳",
            color = if (isDone) Color(0xFF4CAF50) else Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}