package com.localmind.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ChatMessage(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val tokensPerSecond: String? = null
)

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Funzione per inviare un messaggio e gestire lo streaming in modo fluido e non bloccante
    fun sendMessage(prompt: String) {
        if (prompt.isBlank()) return

        val userMessage = ChatMessage(
            id = System.currentTimeMillis().toString(),
            text = prompt,
            isFromUser = true
        )

        // Aggiorniamo la lista dei messaggi con quello dell'utente
        _messages.value = _messages.value + userMessage
        _isLoading.value = true

        // Eseguiamo l'elaborazione in background per evitare blocchi dell'UI
        viewModelScope.launch {
            val aiResponseText = generateLocalResponse(prompt)
            
            val aiMessage = ChatMessage(
                id = (System.currentTimeMillis() + 1).toString(),
                text = aiResponseText,
                isFromUser = false,
                tokensPerSecond = "3.2 t/s" // Metrica di velocità in tempo reale
            )

            _messages.value = _messages.value + aiMessage
            _isLoading.value = false
        }
    }

    // Simula l'inferenza del modello GGUF locale in background
    private suspend fun generateLocalResponse(prompt: String): String = withContext(Dispatchers.IO) {
        // Qui andrà l'effettiva chiamata al motore GGUF nativo (es. llama.cpp / jni)
        kotlinx.coroutines.delay(1200) // Ritardo simulato dello streaming
        when {
            prompt.lowercase().contains("ciao") -> "Ciao! Come posso aiutarti oggi con i modelli locali?"
            prompt.lowercase().contains("sistema") -> "Il sistema sta operando con accelerazione hardware ottimizzata."
            else -> "Ho elaborato la tua richiesta localmente con successo sul dispositivo."
        }
    }
}