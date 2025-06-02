package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.domain.usecase.TonWalletUseCase

class NewWalletViewModel(
    private val tonWalletUseCase: TonWalletUseCase
) : ViewModel() {
    val isLoadingText = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val snackBarText = mutableStateOf("")
    val isCopied = mutableStateOf(false)
    val showBackupAlert = mutableStateOf(false)
    val secretKeys = mutableStateOf(List(24) { "" })
    val userFriendlyAddress = mutableStateOf("")

    init {
        isLoading.value = true
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val seedPhrases = (tonWalletUseCase.generateMnemonics(24))
                secretKeys.value = seedPhrases
                val result = tonWalletUseCase.generateWallet(secretKeys.value)
                if (result.success) {
                    userFriendlyAddress.value = result.result?.userFriendlyAddress.toString()
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
            // create account n goto home screen


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

    fun initAccount() {
        isLoading.value = true

    }
}