package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ClipboardManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.data.dataModels.Password
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.domain.usecase.FormatStringUseCase
import leo.decentralized.tonchat.domain.usecase.TonWalletUseCase
import leo.decentralized.tonchat.domain.usecase.UserUseCase


class ImportWalletViewModel(
    private val formatStringUseCase: FormatStringUseCase,
    private val tonWalletUseCase: TonWalletUseCase,
    private val secureStorageRepository: SecureStorageRepository,
    private val userUseCase: UserUseCase,
    private val password : Password
) : ViewModel() {
    val secretKeys = mutableStateOf(List(24) { "" })
    val isSecretKeys24 = mutableStateOf(false)
    val isSecretKeysValid = derivedStateOf {
        if (isSecretKeys24.value) {
            (!secretKeys.value.contains(""))
        } else {
            (!secretKeys.value.slice(0..11).contains(
                ""
            ))
        }
    }
    val isLoadingText = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val snackBarText = mutableStateOf("")

    @OptIn(ExperimentalStdlibApi::class)
    fun importWallet() {
        isLoading.value = true
        viewModelScope.launch {
            val wallet = tonWalletUseCase.generateWallet(secretKeys = if (isSecretKeys24.value) secretKeys.value else secretKeys.value.slice(0..11))
            if (wallet.success) {
                snackBarText.value = wallet.result?.publicKeyHex ?: ""
                    val privateKey = wallet.result?.privateKey ?: ByteArray(1)
                    val publicKey = wallet.result?.publicKeyHex.toString()
                    val userFriendlyAddress = wallet.result?.userFriendlyAddress.toString()
                    secureStorageRepository.storeUserFriendlyAddress(userFriendlyAddress)
                    secureStorageRepository.storePublicKey(publicKey.hexToByteArray())
                    secureStorageRepository.storePrivateKey(privateKey)
                initAccount()
            } else {
                snackBarText.value = wallet.error?.message.toString()
                //forward the non null cause to developers
            }
            isLoading.value = false
        }

    }

    fun toggleSecretKeys24() {
        println("password is : " + password.password)
        password.password = "changed"
        isSecretKeys24.value = !isSecretKeys24.value
    }

    fun pasteSecretKeys(clipboard: ClipboardManager) {
        viewModelScope.launch {
            if (clipboard.hasText()) {
                val clip = clipboard.getText()
                pasteSecretKeys(clipData = clip?.text.toString())
            }
        }
    }

    fun pasteSecretKeys(keyIndex: Int = 0, clipData: String) {
        val keys = formatStringUseCase.splitStringBySpace(clipData)

        var currentIndex = keyIndex
        var used = 0
        fun addAll() {
            secretKeys.value = secretKeys.value.toMutableList().also {
                it[currentIndex] =
                    keys.slice(used..<keys.size).joinToString(separator = " ")
            }
        }
        for (i in keys.indices) {
            if (currentIndex == if (isSecretKeys24.value) 23 else 11) {
                used = i
                addAll()
            }
            if (secretKeys.value[currentIndex] == "") {
                secretKeys.value = secretKeys.value.toMutableList().also {
                    it[currentIndex] = keys[i]
                }
            } else {
                used = i
                addAll()
                break
            }
            currentIndex++
        }
    }

    fun checkWalletPhrase(phrase: String): Boolean {
        return tonWalletUseCase.isWalletPhraseValid(phrase)
    }

    fun initAccount(){
        userUseCase.importWallet()

        isLoadingText.value = "Initiating account..."
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
           /* val signedMsg = tonWalletUseCase.signMessage(privateKey, userFriendlyAddress.value)
            if (signedMsg.success) {
                val result = tonWalletUseCase.generateNewToken(
                    publicKey,
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
            }*/
        }


    }
}