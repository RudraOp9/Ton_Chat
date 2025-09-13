package leo.decentralized.tonchat.data.repositories.security

import android.util.Base64
import leo.decentralized.tonchat.data.dataModels.Password
import leo.decentralized.tonchat.utils.Effect
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.ExperimentalEncodingApi

class PasswordEncryptionRepositoryImpl(private val password: Password): PasswordEncryptionRepository {

    private fun generateSecureKey(password: String?): SecretKey {
        val salt = "TON_DECENTRALIZED_CHAT".toByteArray()
        val iterationCount = 65536
        val keyLength = 256 // AES-256

        val spec: KeySpec = PBEKeySpec(password?.toCharArray(), salt, iterationCount, keyLength)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    override fun encrypt(stringToEncrypt: String): Effect<String> {
        return try {
            val secretKey = generateSecureKey(password.password)
            Effect(true,encryptAES(stringToEncrypt, secretKey.encoded))
        } catch (e: Exception) {
            Effect(false,error = e )
        }
    }

    override fun decrypt(stringToDecrypt: String): Effect<String> {
        return try {
            val secretKey = generateSecureKey(password.password)
            Effect(true,decryptAES(stringToDecrypt, secretKey.encoded))
        } catch (e: Exception) {
            Effect(false,error = e )
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun encryptAES(plainText: String, key: ByteArray): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKey: SecretKey = SecretKeySpec(key, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT) + ":" + Base64.encodeToString(iv, Base64.DEFAULT)
    }

    fun decryptAES(encryptedText: String, key: ByteArray): String {
        val parts = encryptedText.split(":")
        val encryptedData = Base64.decode(parts[0], Base64.DEFAULT)
        val iv = Base64.decode(parts[1], Base64.DEFAULT)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKey: SecretKey = SecretKeySpec(key, "AES")
        val gcmParameterSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)
        val decryptedBytes = cipher.doFinal(encryptedData)
        return String(decryptedBytes)
    }
}