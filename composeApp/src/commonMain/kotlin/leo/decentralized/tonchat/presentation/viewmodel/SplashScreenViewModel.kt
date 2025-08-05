package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.navigation.Screens

class SplashScreenViewModel(secureStorageRepository: SecureStorageRepository) : ViewModel() {
    val isLoading = mutableStateOf(true)
    val screen = mutableStateOf(Screens.Welcome.screen)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            secureStorageRepository.getLoggedIn().onSuccess {
                println("isLoggedIn : $it")
                if (it) {
                    screen.value = Screens.HomeScreen.screen
                }else{
                    screen.value = Screens.Welcome.screen
                }
                isLoading.value = false
            }.onFailure {
                isLoading.value = false
                it.printStackTrace()
            }
        }
    }
}