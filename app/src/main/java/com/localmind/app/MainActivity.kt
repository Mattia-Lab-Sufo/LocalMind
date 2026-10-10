package com.localmind.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF121212)
private val Panel = Color(0xFF202020)
private val Accent = Color(0xFFB5A0FF)
private val MainText = Color(0xFFF4F4F4)
private val Muted = Color(0xFFAAAAAA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = Background,
                    surface = Panel,
                    primary = Accent
                )
            ) {
                LocalMindApp()
            }
        }
    }
}

private enum class Page(val label: String) {
    CHAT("Chat"),
    EXPLORE("Explorer"),
    LIBRARY("Libreria"),
    DEVICE("Dispositivo")
}

private data class Model(
    val name: String,
    val size: String,
    val description: String,
    val installed: Boolean = false,
    val uncensored: Boolean = false
)

private val sampleModels = listOf(
    Model("Small Instruct", "1.2 GB", "Leggero · Per telefoni con risorse limitate"),
    Model("Balanced Instruct", "3.5 GB", "Bilanciato · Qualità e velocità"),
    Model("Advanced Instruct", "7 GB", "Avanzato · Richiede molta memoria"),
    Model("Community Unfiltered", "2.4 GB", "Modello community · Verificare licenza e requisiti", uncensored = true)
)

@Composable
private fun LocalMindApp() {
    var page by remember { mutableStateOf(Page.CHAT) }
    var selectedModel by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavigationBar(containerColor = Panel) {
                Page.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = page == destination,
                        onClick = { page = destination },
                        icon = {
                            Icon(
                                imageVector = when (destination) {
                                    Page.CHAT -> Icons.Default.Chat
                                    Page.EXPLORE -> Icons.Default.Search
                                    Page.LIBRARY -> Icons.Default.Inventory2
                                    Page.DEVICE -> Icons.Default.Memory
                                },
                                contentDescription = destination.label,
                                tint = if (page == destination) Accent else Muted
                            )
                        },
                        label = { Text(destination.label, color = if (page == destination) Accent else Muted) }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "LocalMind",
                    color = MainText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                Text(
                    selectedModel ?: "Scegli un modello",
                    color = Accent,
                    modifier = Modifier.clickable {
                        page = Page.LIBRARY
                    }
                )
            }

            when (page) {
                Page.CHAT -> ChatPage(selectedModel)
                Page.EXPLORE -> ExplorerPage(
                    onSelect = { model ->
                        selectedModel = model
                        page = Page.CHAT
                    }
                )
                Page.LIBRARY -> LibraryPage(
                    selectedModel = selectedModel,
                    onSelect = { selectedModel = it }
                )
                Page.DEVICE -> DevicePage()
            }
        }
    }
}

@Composable
private fun ChatPage(model: String?) {
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("La tua AI, sul tuo dispositivo.",
                color = MainText, fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold)
            Text(
                if (model == null)
                    "Scarica un modello dall'Explorer per iniziare."
                else "Modello selezionato: $model",
                color = Muted
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Panel, RoundedCornerShape(28.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message,
                onValueChange = { message = it },
                placeholder = { Text("Scrivi un messaggio...", color = Muted) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = MainText,
                    unfocusedTextColor = MainText
                )
            )
            IconButton(onClick = {
                message = ""
            }) {
                Icon(Icons.Default.Send, "Invia", tint = Accent)
            }
        }
    }
}

@Composable
private fun ExplorerPage(onSelect: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    val showUncensored = query.trim().equals("UN:CENSURATI", ignoreCase = true)

    val results = sampleModels.filter { model ->
        if (showUncensored) {
            model.uncensored
        } else {
            !model.uncensored && (query.isBlank() || model.name.contains(query, ignoreCase = true))
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Cerca modelli o scrivi UN:CENSURATI", color = Muted) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Muted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MainText,
                unfocusedTextColor = MainText,
                focusedBorderColor = Accent,
                unfocusedBorderColor = Muted
            )
        )

        Spacer(Modifier.height(16.dp))

        Text(
            if (showUncensored) "Modelli con minori restrizioni" else "Esplora i modelli",
            color = MainText, fontSize = 20.sp, fontWeight = FontWeight.Bold
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(results, key = { it.name }) { model ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Panel)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(model.name, color = MainText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(model.size, color = Accent, fontSize = 14.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(model.description, color = Muted, fontSize = 14.sp)
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { onSelect(model.name) },
                            colors = ButtonDefaults.buttonColors(containerColor = Accent)
                        ) {
                            Text("Seleziona modello", color = Background)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryPage(selectedModel: String?, onSelect: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("La tua libreria", color = MainText, fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text(
            selectedModel?.let { "Modello scelto: $it" } ?: "Non hai ancora selezionato un modello.",
            color = Muted
        )
    }
}

@Composable
private fun DevicePage() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Il tuo dispositivo", color = MainText, fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("Versione Android: ${android.os.Build.VERSION.RELEASE}", color = MainText)
        Spacer(Modifier.height(8.dp))
        Text("Compatibilità RAM e processore: Rilevamento automatico attivo.", color = Muted)
    }
}