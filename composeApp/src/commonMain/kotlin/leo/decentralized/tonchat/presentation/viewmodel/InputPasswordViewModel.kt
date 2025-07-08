package leo.decentralized.tonchat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import leo.decentralized.tonchat.data.dataModels.Password
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository

class InputPasswordViewModel(
    private val secureStorageRepository: SecureStorageRepository,
    private val password: Password
) : ViewModel() {
    fun checkPassAndContinue(pass: String): Result<Boolean> {
        val token = secureStorageRepository.getToken()
        return if (token.isSuccess) {
            savePassword(pass)
            Result.success(true)
        } else {
            Result.failure(Exception("Wrong password"))
        }
    }

    fun savePassword(pass: String) {
        password.password = pass
    }

}