package leo.decentralized.tonchat.data.repositories.security

import com.russhwolf.settings.Settings
import io.ktor.util.hex

class SecureStorageRepositoryImpl(
    private val settings: Settings,
    private val passwordEncryptionRepository: PasswordEncryptionRepository
):SecureStorageRepository  {

    override fun storeToken(token: String): Result<Boolean> {
        try {
            val result = passwordEncryptionRepository.encrypt(token)
            if (!result.success) throw result.error!!
            settings.putString("token", result.result!!).apply {
                return Result.success(true)
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun getToken(): Result<String> {
        try {
            settings.getStringOrNull("token").let {
                if (it == null) return Result.failure(Exception("Token not found"))
                val result = passwordEncryptionRepository.decrypt(it)
                if (!result.success) throw result.error!!
                return Result.success(result.result!!)
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun deleteToken(): Result<Boolean> {
        try {
            settings.remove("token").apply {
                return Result.success(true)
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun storePrivateKey(privateKey: ByteArray): Result<Boolean> {
        try {
            val result = passwordEncryptionRepository.encrypt(hex(privateKey))
            if (!result.success) throw result.error!!
            settings.putString("privateKey", result.result!!).apply {
                return Result.success(true)
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun getPrivateKey(): Result<ByteArray> {
        try {
            settings.getStringOrNull("privateKey").let {
                if (it == null) return Result.failure(Exception("privateKey not found"))
                val result = passwordEncryptionRepository.decrypt(it)
                if (!result.success) throw result.error!!
                return Result.success(hex(result.result!!))
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun deletePrivateKey(): Result<Boolean> {
        try {
            settings.remove("privateKey").apply {
                return Result.success(true)
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun storePublicKey(publicKey: ByteArray): Result<Boolean> {
        try {
            val result = passwordEncryptionRepository.encrypt(hex(publicKey))
            if (!result.success) throw result.error!!
            settings.putString("publicKey", result.result!!).apply {
                return Result.success(true)
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun getPublicKey(): Result<ByteArray> {
        try {
            settings.getStringOrNull("publicKey").let {
                if (it == null) return Result.failure(Exception("publicKey not found"))
                val result = passwordEncryptionRepository.decrypt(it)
                if (!result.success) throw result.error!!
                return Result.success(hex(result.result!!))
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun deletePublicKey(): Result<Boolean> {
        try {
            settings.remove("publicKey").apply {
                return Result.success(true)
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun storeUserFriendlyAddress(userFriendlyAddress: String): Result<Boolean> {
        try {
            val result = passwordEncryptionRepository.encrypt(userFriendlyAddress)
            if (!result.success) throw result.error!!
            settings.putString("userFriendlyAddress", result.result!!).apply {
                return Result.success(true)
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun getUserFriendlyAddress(): Result<String> {
        try {
            settings.getStringOrNull("userFriendlyAddress").let {
                if (it == null) return Result.failure(Exception("Address not found"))
                val result = passwordEncryptionRepository.decrypt(it)
                if (!result.success) throw result.error!!
                return Result.success(result.result!!)
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun deleteUserFriendlyAddress(): Result<Boolean> {
        try {
            settings.remove("userFriendlyAddress").apply {
                return Result.success(true)
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

}