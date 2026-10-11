package com.localmind.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Chat : Screen("chat", "Chat", Icons.Default.Chat)
    object Explorer : Screen("explorer", "Explorer", Icons.Default.CompassCalibration)
    object Library : Screen("library", "Libreria", Icons.Default.Folder)
    object Device : Screen("device", "Dispositivo", Icons.Default.DeviceHub)
}

@Composable
fun MainScreen() {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Chat) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF161B22),
                contentColor = Color.White
            ) {
                val items = listOf(
                    Screen.Chat,
                    Screen.Explorer,
                    Screen.Library,
                    Screen.Device
                )

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF00B0FF),
                            selectedTextColor = Color(0xFF00B0FF),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0xFF0D1117)
                        )
                    )
                }
            }
        },
        containerColor = Color(0xFF0D1117)
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color(0xFF0D1117)
        ) {
            when (selectedScreen) {
                is Screen.Chat -> ChatScreen()
                is Screen.Explorer -> ExplorerScreen()
                is Screen.Library -> BoxScreenPlaceholder("La tua libreria è vuota")
                is Screen.Device -> BoxScreenPlaceholder("Informazioni dispositivo e hardware")
            }
        }
    }
}

@Composable
fun BoxScreenPlaceholder(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.Gray, fontSize = 16.sp)
    }
}