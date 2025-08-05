package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.data.dataModels.Contact
import leo.decentralized.tonchat.data.dataModels.Password
import leo.decentralized.tonchat.domain.usecase.ChatUseCase
import leo.decentralized.tonchat.navigation.PassCode
import leo.decentralized.tonchat.navigation.Screens

class ChatViewModel(
    private val chatUseCase: ChatUseCase,
    private val password: Password
): ViewModel() {
    var contacts = mutableStateOf<List<Contact>>(emptyList())

    val isLoadingText = mutableStateOf("")
    val isLoading = mutableStateOf(true)
    val snackBarText = mutableStateOf("")
    init {
        viewModelScope.launch(Dispatchers.IO){
            chatUseCase.getContacts().onSuccess {
                println("Contacts: $it")
                isLoading.value = false
                contacts.value = it
            }.onFailure {
                isLoading.value = false
                snackBarText.value = it.message.toString()
            }
        }
    }

    fun checkPassword(navController: NavController){
        if (password.password == null){
            navController.navigate(PassCode(false, Screens.HomeScreen.screen)){
                popUpTo(Screens.HomeScreen.screen){inclusive = true}
            }
        }
    }
}