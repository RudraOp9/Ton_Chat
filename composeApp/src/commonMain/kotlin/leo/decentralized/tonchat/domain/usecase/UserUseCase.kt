package leo.decentralized.tonchat.domain.usecase

import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.data.repositories.network.userApi.UserApiRepository
import leo.decentralized.tonchat.utils.Result

class UserUseCase(
    private val userApiRepository: UserApiRepository,
    private val secureStorageRepository: SecureStorageRepository
) {
    @OptIn(ExperimentalStdlibApi::class)
    suspend fun createNewUser(): Result<Boolean> {
        val publicKey = secureStorageRepository.getPublicKey()
        val address = secureStorageRepository.getUserFriendlyAddress()
        if ( publicKey.isSuccess && address.isSuccess) {
            val response =
                userApiRepository.newUser(
                    publicKey = (publicKey.getOrNull() ?: ByteArray(0)).toHexString(),
                    address = address.getOrNull() ?: ""
                )

            return if (response.success){
                Result(true,true)
            }else{
                Result(false, error = response.error?: Exception("Something went wrong"))
            }
        } else {
            return Result(false, error = Exception(""))
        }
    }

    fun importWallet():Result<Boolean> {
        TODO("Not yet implemented")
    }

    suspend fun checkUserExist(address:String):Result<Boolean> {
        val response = userApiRepository.checkUserExist(address)
        return if (response.success){
            Result(true,response.result)
        }else{
            Result(false, error = response.error?: Exception("Something went wrong"))
        }
    }
}