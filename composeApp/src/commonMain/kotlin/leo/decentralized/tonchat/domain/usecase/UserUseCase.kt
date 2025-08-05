package leo.decentralized.tonchat.domain.usecase

import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.data.repositories.network.userApi.UserApiRepository
import leo.decentralized.tonchat.utils.Effect

class UserUseCase(
    private val userApiRepository: UserApiRepository,
    private val secureStorageRepository: SecureStorageRepository
) {
    @OptIn(ExperimentalStdlibApi::class)
    suspend fun createNewUser(): Effect<Boolean> {
        val publicKey = secureStorageRepository.getPublicKey()
        val address = secureStorageRepository.getUserFriendlyAddress()
        if ( publicKey.isSuccess && address.isSuccess) {
            val response =
                userApiRepository.newUser(
                    publicKey = (publicKey.getOrNull() ?: ByteArray(0)).toHexString(),
                    address = address.getOrNull() ?: ""
                )

            return if (response.success){
                secureStorageRepository.setLoggedIn(true)
                Effect(success = true, result = true)
            }else{
                Effect(false, error = response.error?: Exception("Something went wrong"))
            }
        } else {
            return Effect(false, error = Exception(""))
        }
    }

    fun importWallet():Effect<Boolean> {
        TODO("Not yet implemented")
    }

    suspend fun checkUserExist(address:String):Effect<Boolean> {
        val response = userApiRepository.checkUserExist(address)
        return if (response.success){
            secureStorageRepository.setLoggedIn(true)
            Effect(true,response.result)
        }else{
            Effect(false, error = response.error?: Exception("Something went wrong"))
        }
    }
}