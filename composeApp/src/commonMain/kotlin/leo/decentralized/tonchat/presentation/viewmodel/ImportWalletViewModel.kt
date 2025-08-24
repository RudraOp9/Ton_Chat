package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ClipboardManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.data.dataModels.Password
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.domain.usecase.FormatStringUseCase
import leo.decentralized.tonchat.domain.usecase.TonWalletUseCase
import leo.decentralized.tonchat.domain.usecase.UserUseCase
import leo.decentralized.tonchat.presentation.navigation.Screens


class ImportWalletViewModel(
    private val formatStringUseCase: FormatStringUseCase,
    private val tonWalletUseCase: TonWalletUseCase,
    private val secureStorageRepository: SecureStorageRepository, // todo : implement use case for this or use in existing one.
    private val userUseCase: UserUseCase,
    private val password: Password
) : ViewModel() {
    val secretKeys = mutableStateOf(List(24) { "" })
    val isSecretKeys24 = mutableStateOf(true)
    val isSecretKeysValid = derivedStateOf {
        if (isSecretKeys24.value) {
            (!secretKeys.value.contains(""))
        } else {
            (!secretKeys.value.slice(0..11).contains(
                ""
            ))
        }
    }

    private lateinit var userFriendlyAddress: String
    val isLoadingText = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val snackBarText = mutableStateOf("")

    @OptIn(ExperimentalStdlibApi::class)
    fun importWallet(navController: NavController) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.Default){
            val wallet = tonWalletUseCase.generateWallet(
                secretKeys = if (isSecretKeys24.value) secretKeys.value else secretKeys.value.slice(
                    0..11
                )
            )
            if (wallet.success) {
                val privateKey = wallet.result?.privateKey ?: ByteArray(1)
                val publicKey = wallet.result?.publicKeyHex.toString()
                userFriendlyAddress = wallet.result?.userFriendlyAddress.toString()
                secureStorageRepository.storeUserFriendlyAddress(userFriendlyAddress)
                secureStorageRepository.storePublicKey(publicKey.hexToByteArray())
                secureStorageRepository.storePrivateKey(privateKey)
                initAccount(navController)
            } else {
                snackBarText.value = wallet.error?.message.toString()
                isLoading.value = false
            }
        }
    }

    fun toggleSecretKeys24() {
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

    fun initAccount(navController: NavController) {
        isLoadingText.value = "Initiating account..."
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val signedMsg = tonWalletUseCase.signMessage(userFriendlyAddress)
            if (signedMsg.success) {
                val result = tonWalletUseCase.generateNewToken(
                    userFriendlyAddress,
                    signedMsg.result.toString()
                )
                if (result.success) {
                    println("token " + result.result)
                    result.result?.let {
                        secureStorageRepository.storeToken(token = it).onSuccess {
                            val existStatus = userUseCase.checkUserExist(userFriendlyAddress)
                            if (existStatus.success) {
                                // User exists, proceed with login or other actions
                                if (existStatus.result == true) {
                                    isLoadingText.value = "Account exists. Logging in..."
                                    launch(Dispatchers.Main){
                                        navController.navigate(Screens.HomeScreen.screen){
                                            popUpTo(Screens.Welcome.screen){inclusive = true}
                                        }
                                    }
                                } else {
                                    isLoadingText.value = "Initiating Account..."
                                    val newUserStatus = userUseCase.createNewUser()
                                    if (newUserStatus.success) {
                                        launch(Dispatchers.Main){
                                            navController.navigate(Screens.HomeScreen.screen){
                                                popUpTo(Screens.Welcome.screen){inclusive = true}
                                            }
                                        }
                                    } else {
                                        snackBarText.value =
                                            newUserStatus.error?.message ?: "Something went wrong"
                                    }
                                }
                            } else {
                                isLoading.value = false
                                snackBarText.value =
                                    existStatus.error?.message ?: "Something went wrong"
                            }
                        }.onFailure { it ->
                            snackBarText.value = it.message.toString()
                            isLoading.value = false
                            isLoadingText.value = ""
                        }
                    }
                } else {
                    isLoading.value = true
                    snackBarText.value = signedMsg.error?.message ?: ""
                }
            } else {
                isLoading.value = true
                snackBarText.value = signedMsg.error?.message ?: ""
            }
        }
    }
}