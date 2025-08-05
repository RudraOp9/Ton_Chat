package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import leo.decentralized.tonchat.data.dataModels.Password
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository

class InputPasswordViewModel(
    private val secureStorageRepository: SecureStorageRepository,
    private val password: Password
) : ViewModel() {
    val enteredPin = mutableStateOf("")

    fun checkPassAndContinue(): Result<Boolean> {
        val token = secureStorageRepository.getToken()
        return if (token.isSuccess) {
            Result.success(true)
        } else {
            Result.failure(Exception("Wrong password"))
        }
    }

    fun savePassword() {
        password.password = enteredPin.value
    }

}