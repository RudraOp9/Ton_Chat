package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.data.dataModels.Contact
import leo.decentralized.tonchat.domain.usecase.ChatUseCase

class ChatViewModel(
    private val chatUseCase: ChatUseCase
): ViewModel() {
    var contacts = mutableStateOf<List<Contact>>(emptyList())

    val isLoadingText = mutableStateOf("")
    val isLoading = mutableStateOf(true)
    val snackBarText = mutableStateOf("")
    init {
        viewModelScope.launch(Dispatchers.IO){
            chatUseCase.getContacts().onSuccess {
                isLoading.value = false
                contacts.value = it
            }.onFailure {
                isLoading.value = false
                snackBarText.value = it.message.toString()
            }
        }

    }
}