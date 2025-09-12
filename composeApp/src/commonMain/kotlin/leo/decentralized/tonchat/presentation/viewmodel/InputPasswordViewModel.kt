package leo.decentralized.tonchat.presentation.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import leo.decentralized.tonchat.data.dataModels.Password
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository

class InputPasswordViewModel(
    private val secureStorageRepository: SecureStorageRepository,
    private val password: Password
) : ViewModel() {
    private val _enteredPin = MutableStateFlow("")
    val enteredPin = _enteredPin.asStateFlow()
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

    fun changePin(int: Int){
        _enteredPin.value += int
    }
    fun backspace(){
        _enteredPin.value = _enteredPin.value.dropLast(1)
    }
    fun clearPin(){
        _enteredPin.value = ""
    }

}