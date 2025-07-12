package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.domain.usecase.TonWalletUseCase
import leo.decentralized.tonchat.domain.usecase.UserUseCase

@OptIn(ExperimentalStdlibApi::class)
class NewWalletViewModel(
    private val tonWalletUseCase: TonWalletUseCase,
    private val userUseCase: UserUseCase,
    val secureStorageRepository : SecureStorageRepository
) : ViewModel() {
    val isLoadingText = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val snackBarText = mutableStateOf("")
    val isCopied = mutableStateOf(false)
    val showBackupAlert = mutableStateOf(false)
    val secretKeys = mutableStateOf(List(24) { "" })
    val userFriendlyAddress = mutableStateOf("")
    lateinit var privateKey: ByteArray
    var publicKey = ""

    init {
        isLoading.value = true
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val seedPhrases = (tonWalletUseCase.generateMnemonics(24))
                secretKeys.value = seedPhrases
                val result = tonWalletUseCase.generateWallet(secretKeys.value)
                if (result.success) {
                    privateKey = result.result?.privateKey ?: ByteArray(1)
                    publicKey = result.result?.publicKeyHex.toString()
                    userFriendlyAddress.value = result.result?.userFriendlyAddress.toString()
                    secureStorageRepository.storeUserFriendlyAddress(userFriendlyAddress.value)
                    secureStorageRepository.storePublicKey(publicKey.hexToByteArray())
                    secureStorageRepository.storePrivateKey(privateKey)
                } else {
                    snackBarText.value = result.error?.message.toString()
                }
                isLoading.value = false
            }
        } catch (e: Exception) {
            snackBarText.value = e.message.toString()
            isLoading.value = false
        }
    }

    fun canContinue() {
        if (isCopied.value) {
            isLoadingText.value = "Initiating account..."
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val signedMsg = tonWalletUseCase.signMessage(userFriendlyAddress.value)
                if (signedMsg.success) {
                    val result = tonWalletUseCase.generateNewToken(
                        userFriendlyAddress.value,
                        signedMsg.result.toString()
                    )
                    if (result.success) {
                        println("token " + result.result)
                        result.result?.let {
                            secureStorageRepository.storeToken(token = it).onSuccess {
                                initAccount()
                                isLoading.value = false
                                snackBarText.value = "Account created successfully"
                            }.onFailure {it->
                                snackBarText.value = it.message.toString()
                                isLoading.value = false
                                isLoadingText.value = ""
                            }
                        }
                    } else {
                        isLoading.value = true
                        snackBarText.value = signedMsg.error?.message?:""
                    }
                } else {
                    isLoading.value = true
                    snackBarText.value = signedMsg.error?.message?:""
                }
            }
        } else {
            showBackupAlert.value = true
        }
    }

    fun copyPhrases(clipManager: ClipboardManager) {
        val clip = secretKeys.value.joinToString(" ")
        clipManager.setText(AnnotatedString(clip)).apply {
            isCopied.value = true
        }
    }

    suspend fun initAccount() {
        val result = userUseCase.createNewUser()
        if(!result.success){
            snackBarText.value = result.error?.message?:"Unknown error"
        }
        isLoading.value = false
        isLoadingText.value = ""
        //login()
    }
}