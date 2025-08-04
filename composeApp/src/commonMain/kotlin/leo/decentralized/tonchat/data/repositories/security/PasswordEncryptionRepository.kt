package leo.decentralized.tonchat.data.repositories.security

import leo.decentralized.tonchat.utils.Effect

interface PasswordEncryptionRepository {
    fun encrypt(stringToEncrypt: String): Effect<String>
    fun decrypt(stringToDecrypt: String): Effect<String>
}