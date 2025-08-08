package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.domain.usecase.ChatUseCase
import leo.decentralized.tonchat.presentation.screens.home.ChatMessage

class ChatViewModel(
    private val chatUseCase: ChatUseCase
) : ViewModel() {
    var shimmer = mutableStateOf(true)
    var chatList = mutableStateOf(listOf<ChatMessage>())
    var snackBarText = mutableStateOf("")
    var chatAddress = ""

    fun getChats(address: String, contactPublicAddress: String) {
        chatAddress = address
        viewModelScope.launch(Dispatchers.IO) {
            chatUseCase.getChatFor(address = address, contactPublicAddress = contactPublicAddress, {
                it.onSuccess {chat ->
                    chatList.value = chatList.value.toMutableList().apply {
                        add(0,chat)
                    }
                }.onFailure {
                    snackBarText.value = it.message ?: "Unknown error"
                }
            }).onSuccess {
                // todo paging
                shimmer.value = false
            }.onFailure {
                snackBarText.value = it.message ?: "Unknown error"
                shimmer.value = false
            }
        }
    }

    fun sendMessage(message: String,contactPublicAddress:String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatList.value = chatList.value.toMutableList().apply {
                add(0, ChatMessage(message, true))
            }
            chatUseCase.sendMessage(message = message, to = chatAddress, contactPublicAddress ).onSuccess {
                //todo handle remove sending icon
            }.onFailure {
                //todo handle add failure icon
            }
        }
    }
}