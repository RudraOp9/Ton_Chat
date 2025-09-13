package leo.decentralized.tonchat.presentation.viewmodel

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
import leo.decentralized.tonchat.presentation.navigation.Screen
import leo.decentralized.tonchat.presentation.navigation.Screens

class HomeViewModel(
    private val chatUseCase: ChatUseCase,
    private val password: Password
): ViewModel() {
    var contacts = mutableStateOf<List<Contact>>(emptyList())
    var spamContact = mutableStateOf<List<Contact>>(emptyList())
    var newContactScreen = mutableStateOf(false)

    val isLoadingText = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val isContactsLoading = mutableStateOf(true)
    val snackBarText = mutableStateOf("")
    init {
        viewModelScope.launch(Dispatchers.IO){
            chatUseCase.getContacts().onSuccess {
                isLoading.value = false
                isContactsLoading.value = false
                contacts.value = it.contacts?: emptyList()
                spamContact.value = it.spamContacts?: emptyList()
            }.onFailure {
                isLoading.value = false
                isContactsLoading.value = false
                snackBarText.value = it.message.toString()
            }
        }
    }

    fun checkPassword(navController: NavController){
        if (password.password == null){
            navController.navigate(Screen.PassCode(false, Screens.HomeScreen.screen)){
                popUpTo(Screens.HomeScreen.screen){inclusive = true}
            }
        }
    }

    fun searchAndAddNewContact(address:String){
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            chatUseCase.newContact(address).onSuccess { it ->
                if (it){
                    newContactScreen.value = false
                    isContactsLoading.value = true
                    chatUseCase.getContacts().onSuccess {contact ->
                        contacts.value = contact.contacts?: emptyList()
                        spamContact.value = contact.spamContacts?: emptyList()
                        isLoading.value = false
                        isContactsLoading.value = false
                    }.onFailure {
                        snackBarText.value = it.message.toString()
                        isLoading.value = false
                        isContactsLoading.value = false
                    }
                }
            }.onFailure {
                snackBarText.value = it.message.toString()
                isLoading.value = false
                isContactsLoading.value = false
            }
        }
    }
}