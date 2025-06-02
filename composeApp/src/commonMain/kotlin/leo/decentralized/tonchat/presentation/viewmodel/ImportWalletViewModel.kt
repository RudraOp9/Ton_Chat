package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ClipboardManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.domain.usecase.FormatStringUseCase
import leo.decentralized.tonchat.domain.usecase.TonWalletUseCase


class ImportWalletViewModel(
    private val formatStringUseCase: FormatStringUseCase,
    private val tonWalletUseCase: TonWalletUseCase
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

    fun importWallet() {
        isLoading.value = true
        viewModelScope.launch {
            delay(2000)
            val wallet = tonWalletUseCase.generateWallet(secretKeys = if (isSecretKeys24.value) secretKeys.value else secretKeys.value.slice(0..11))
            if (wallet.success) {
                snackBarText.value = wallet.result?.publicKeyHex ?: ""
            } else {
                snackBarText.value = wallet.error?.message.toString()
                //forward the non null cause to developers
            }
            isLoading.value = false
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
}