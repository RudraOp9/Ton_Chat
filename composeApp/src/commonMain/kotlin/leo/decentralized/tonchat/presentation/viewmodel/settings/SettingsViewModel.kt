package leo.decentralized.tonchat.presentation.viewmodel.settings

import androidx.lifecycle.ViewModel
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository

class SettingsViewModel(private val secureStorageRepository: SecureStorageRepository): ViewModel() {
    fun changeTheme(theme: Int){
        secureStorageRepository.setTheme(theme).onSuccess {
            secureStorageRepository.refreshTheme()
        }.onFailure {
            it.printStackTrace()
        }
    }
}