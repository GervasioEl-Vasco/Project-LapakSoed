package com.example.lapaksoed.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksoed.data.remote.ConversationResponse
import com.example.lapaksoed.data.remote.MessageResponse
import com.example.lapaksoed.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed class ChatListState {
    object Loading : ChatListState()
    data class Success(val conversations: List<ConversationResponse>) : ChatListState()
    data class Error(val error: String) : ChatListState()
}

sealed class ChatDetailState {
    object Idle : ChatDetailState()
    object Loading : ChatDetailState()
    data class Success(val messages: List<MessageResponse>) : ChatDetailState()
    data class Error(val error: String) : ChatDetailState()
}

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()

    private val _listState = MutableStateFlow<ChatListState>(ChatListState.Loading)
    val listState: StateFlow<ChatListState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow<ChatDetailState>(ChatDetailState.Idle)
    val detailState: StateFlow<ChatDetailState> = _detailState.asStateFlow()

    private val _activeConversation = MutableStateFlow<ConversationResponse?>(null)
    val activeConversation: StateFlow<ConversationResponse?> = _activeConversation.asStateFlow()
    
    fun loadConversations() {
        viewModelScope.launch {
            _listState.value = ChatListState.Loading
            try {
                val response = repository.getConversations()
                if (response.isSuccessful) {
                    _listState.value = ChatListState.Success(response.body() ?: emptyList())
                } else {
                    _listState.value = ChatListState.Error("Failed to load chats")
                }
            } catch (e: Exception) {
                _listState.value = ChatListState.Error(e.message ?: "Network error")
            }
        }
    }

    fun selectConversation(conversation: ConversationResponse) {
        _activeConversation.value = conversation
        loadMessages(conversation.id)
    }

    fun loadMessages(conversationId: UUID) {
        viewModelScope.launch {
            _detailState.value = ChatDetailState.Loading
            try {
                val response = repository.getMessages(conversationId)
                if (response.isSuccessful) {
                    val msgs = response.body() ?: emptyList()
                    // Sort from oldest to newest for UI
                    _detailState.value = ChatDetailState.Success(msgs.sortedBy { it.createdAt })
                } else {
                    _detailState.value = ChatDetailState.Error("Failed to load messages")
                }
            } catch (e: Exception) {
                _detailState.value = ChatDetailState.Error(e.message ?: "Network error")
            }
        }
    }

    fun sendMessage(conversationId: UUID, body: String) {
        if (body.isBlank()) return
        viewModelScope.launch {
            try {
                val response = repository.sendMessage(conversationId, body)
                if (response.isSuccessful) {
                    // reload messages
                    loadMessages(conversationId)
                }
            } catch (e: Exception) {
                // Ignore for now or handle failed
            }
        }
    }
}
