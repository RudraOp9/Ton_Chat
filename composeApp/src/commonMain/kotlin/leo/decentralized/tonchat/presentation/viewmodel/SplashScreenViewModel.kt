package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.presentation.navigation.Screens

class SplashScreenViewModel(secureStorageRepository: SecureStorageRepository) : ViewModel() {
    val isLoading = mutableStateOf(true)
    val screen = mutableStateOf(Screens.Welcome.screen)
    val theme = secureStorageRepository.currentTheme.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            secureStorageRepository.refreshTheme()
            secureStorageRepository.getLoggedIn().onSuccess {
                if (it) {
                    screen.value = Screens.HomeScreen.screen
                }else{
                    screen.value = Screens.Welcome.screen
                }
                isLoading.value = false
            }.onFailure {
                isLoading.value = false
            }
        }
    }
}