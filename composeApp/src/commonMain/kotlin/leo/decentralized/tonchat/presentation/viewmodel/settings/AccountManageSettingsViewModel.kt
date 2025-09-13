package leo.decentralized.tonchat.presentation.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.data.repositories.security.SecurePrivateExecutionAndStorageRepository
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.domain.usecase.ChatUseCase
import leo.decentralized.tonchat.domain.usecase.UserUseCase
import kotlin.math.log

class AccountManageSettingsViewModel(
    private val secureStorageRepository: SecureStorageRepository,
    private val securePrivateExecutionAndStorageRepository: SecurePrivateExecutionAndStorageRepository,
    private val chatUseCase: ChatUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {

    fun wipeChatsFromServer(){
        viewModelScope.launch(Dispatchers.IO){
            chatUseCase.wipeChats().onSuccess {  }.onFailure {  }
        }
    }

    fun logout(restartApp:()->Unit){
        viewModelScope.launch(Dispatchers.Default) {
            val deleteTokenJob = async { secureStorageRepository.deleteToken() }
            val deletePublicKeyJob = async { secureStorageRepository.deletePublicKey() }
            val deletePrivateKeyJob = async { secureStorageRepository.deletePrivateKey() }
            val deleteUserFriendlyAddressJob = async { secureStorageRepository.deleteUserFriendlyAddress() }
            val setLoggedInJob = async { secureStorageRepository.setLoggedIn(false) }
            val deleteSharedKeysJob = async { securePrivateExecutionAndStorageRepository.removeSharedKeys() }
            listOf(deleteTokenJob, deletePublicKeyJob, deletePrivateKeyJob, deleteUserFriendlyAddressJob, setLoggedInJob,deleteSharedKeysJob).forEach {
                it.await() }
            //todo what about password ?
            viewModelScope.launch(Dispatchers.Main){
                restartApp()
            }
        }
    }

    fun deleteAccount(restartApp:()->Unit){
        viewModelScope.launch(Dispatchers.IO){
            userUseCase.deleteAccount().onSuccess {
                logout(restartApp)
            }.onFailure {  }
        }
    }

}