package com.example.uetontop.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uetontop.data.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

class ChatViewModel : ViewModel() {
    private val repo = ChatRepository()

    private val _messages = MutableStateFlow(
        listOf(
            ChatMessage("Xin chào! Tôi là TinyChat. Tôi luôn sẵn sàng lắng nghe tâm tư của bạn", isUser = false)
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        // Thêm tin nhắn người dùng
        _messages.value = _messages.value + ChatMessage(userText, isUser = true)

        // Gọi API bot
        viewModelScope.launch {
            val reply = repo.getBotReply(userText)
            _messages.value = _messages.value + ChatMessage(reply, isUser = false)
        }
    }
}
