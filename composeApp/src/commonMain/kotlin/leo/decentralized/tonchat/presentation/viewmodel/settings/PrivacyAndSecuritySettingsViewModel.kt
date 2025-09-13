package leo.decentralized.tonchat.presentation.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.domain.usecase.TonWalletUseCase

class PrivacyAndSecuritySettingsViewModel(
    private val secureStorageRepository: SecureStorageRepository,
    private val tonWalletUseCase: TonWalletUseCase
): ViewModel() {

    fun removePrivateKeys(onRemove: ()-> Unit){
        viewModelScope.launch(Dispatchers.Default){
            val deletePublicKeyJob = async { secureStorageRepository.deletePublicKey() }
            val deletePrivateKeyJob = async { secureStorageRepository.deletePrivateKey() }
            listOf(deletePublicKeyJob, deletePrivateKeyJob).forEach {
                it.await() }
            viewModelScope.launch(Dispatchers.Main){
                onRemove()
            }
        }
    }

    fun renewAccessToken(onRenewed: ()-> Unit){
        viewModelScope.launch(Dispatchers.IO){
            val address = secureStorageRepository.getUserFriendlyAddress().getOrThrow()
            val signature = tonWalletUseCase.signMessage(address).result
            if (signature == null){
            }else {
                val newToken = tonWalletUseCase.generateNewToken(address, signature)
                if (newToken.success){
                    secureStorageRepository.storeToken(newToken.result.toString()).onSuccess {
                        viewModelScope.launch(Dispatchers.Main){
                            onRenewed()
                        }
                    }
                }

            }
        }
    }

    fun getLoggedData(){
        //todo

    }
}