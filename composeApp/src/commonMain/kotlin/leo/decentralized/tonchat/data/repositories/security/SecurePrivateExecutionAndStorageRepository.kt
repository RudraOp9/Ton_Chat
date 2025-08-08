package leo.decentralized.tonchat.data.repositories.security

interface SecurePrivateExecutionAndStorageRepository {
    @Deprecated("Can't save ed25519 in keystore")
    suspend fun storePrivateKey(privateKey: ByteArray, publicKey: ByteArray,): Result<Boolean>
    @Deprecated("Can't save ed25519 in keystore")
    fun deletePrivateKey(): Result<Boolean>
    @Deprecated("Can't save ed25519 in keystore")
    fun signMessage(message: String): Result<ByteArray>
    @Deprecated("Can't save ed25519 in keystore")
    fun verifySignature(signature: ByteArray, message: String, publicKey: ByteArray,): Result<Boolean>
    @Deprecated("Can't save ed25519 in keystore")
    fun getPublicKey(): Result<ByteArray>
    fun encryptMessage(message: String,contactPublicKey: ByteArray): Result<String>
    fun decryptMessage(encryptedMessage: String,contactPublicKey: ByteArray): Result<String>
    fun generateSharedKey(contactPublicKey: ByteArray): Result<ByteArray>
}