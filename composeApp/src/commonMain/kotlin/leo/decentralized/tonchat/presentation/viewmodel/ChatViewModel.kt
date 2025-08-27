package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.domain.usecase.ChatUseCase
import leo.decentralized.tonchat.presentation.screens.ChatMessage

class ChatViewModel(
    private val chatUseCase: ChatUseCase
) : ViewModel() {
    var shimmer = mutableStateOf(true)
    var chatList = mutableStateOf(listOf<ChatMessage>())
    var snackBarText = mutableStateOf("")

    fun getChats(contactAddress: String, contactPublicKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatUseCase.getChatFor(contactAddress = contactAddress, contactPublicKey = contactPublicKey,{
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

    fun sendMessage(message: String, contactAddress:String, contactPublicKey:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val chatMsg = ChatMessage(message, isMine = true, isSending = true)
            chatList.value = chatList.value.toMutableList().apply {
                add(0,chatMsg)
            }
            chatUseCase.sendMessage(message = message, to = contactAddress, contactPublicKey ).onSuccess {
                chatList.value = chatList.value.toMutableList().apply {
                    val index = indexOf(chatMsg)
                    set(index,chatList.value[index].copy(isSending = false))
                }
            }.onFailure {
                //todo handle add failure icon
                it.printStackTrace()
            }
        }
    }
}