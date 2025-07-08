package leo.decentralized.tonchat.data.repositories.security

import com.russhwolf.settings.Settings
import io.ktor.util.hex

class SecureStorageRepositoryImpl(private val settings: Settings):SecureStorageRepository  {

    override fun storeToken(token: String): Result<Boolean> {
        try {
            settings.putString("token", token).apply {
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
                return Result.success(it)
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
            settings.putString("privateKey", hex(privateKey)).apply {
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
                return Result.success(hex(it))
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
            settings.putString("publicKey", hex(publicKey)).apply {
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
                return Result.success(hex(it))
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
            settings.putString("userFriendlyAddress", userFriendlyAddress).apply {
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
                return Result.success(it)
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