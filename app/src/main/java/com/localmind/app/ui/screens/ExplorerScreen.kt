package com.localmind.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ModelItem(
    val id: String,
    val name: String,
    val author: String,
    val size: String,
    val ramEstimate: String,
    val tag: String = "TEXT GENERATION",
    val isDownloaded: Boolean = false,
    val downloadProgress: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorerScreen() {
    var searchQuery by remember { mutableStateOf("") }

    // Dati simulati del catalogo modelli (es. Gemma, LFM, MiniCPM)
    val downloadedModels = remember {
        mutableStateListOf(
            ModelItem("1", "Gemma-3-1B-IT-GLM-4.7", "Di: Andycurrent", "1.87 GB", "~2 GB", isDownloaded = true)
        )
    }

    val availableModels = remember {
        mutableStateListOf(
            ModelItem("2", "MiniCPM5-2B-LocalAI", "Di: weareapexcreators", "2.50 GB", "~3 GB"),
            ModelItem("3", "LFM2.5-350M-LocalAI", "Di: weareapexcreators", "362 MB", "~1 GB"),
            ModelItem("4", "LFM2.5-1.2B-Instruct-LocalAI", "Di: weareapexcreators", "1.16 GB", "~2 GB")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MOZZO",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D1117)
                )
            )
        },
        containerColor = Color(0xFF0D1117)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Barra di Ricerca
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cerca modelli...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF161B22),
                        unfocusedContainerColor = Color(0xFF161B22),
                        focusedBorderColor = Color(0xFF00B0FF),
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }

            // Info Hardware (RAM e Architettura)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HardwareChip(text = "⚙️ arm64-v8a")
                    HardwareChip(text = "🧠 7.5 GB RAM (4.4 GB per IA)")
                }
            }

            // Sezione: I Tuoi Modelli
            if (downloadedModels.isNotEmpty()) {
                item {
                    Text(
                        text = "I TUOI MODELLI (${downloadedModels.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }

                items(downloadedModels) { model ->
                    ModelCardItem(model = model, onDownloadClick = {})
                }
            }

            // Sezione: Sfoglia Modelli
            item {
                Text(
                    text = "SFOGLIA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(availableModels) { model ->
                ModelCardItem(
                    model = model,
                    onDownloadClick = {
                        // Logica di avvio download
                    }
                )
            }
        }
    }
}

@Composable
fun HardwareChip(text: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF161B22)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun ModelCardItem(model: ModelItem, onDownloadClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF161B22),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = model.tag,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                if (model.isDownloaded) {
                    Text(
                        text = "PRONTO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = model.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = model.author,
                fontSize = 12.sp,
                color = Color(0xFF00B0FF)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "DIMENSIONI: ${model.size}", fontSize = 11.sp, color = Color.Gray)
                    Text(text = "RAM EST.: ${model.ramEstimate}", fontSize = 11.sp, color = Color.Gray)
                }

                if (!model.isDownloaded) {
                    IconButton(
                        onClick = onDownloadClick,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF007AFF))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download",
                            tint = Color.White
                        )
                    }
                }
            }

            // Barra di avanzamento del Download se attivo
            model.downloadProgress?.let { progress ->
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress / 100f,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF00B0FF),
                    trackColor = Color.DarkGray
                )
                Text(
                    text = "$progress%",
                    fontSize = 11.sp,
                    color = Color.LightGray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}