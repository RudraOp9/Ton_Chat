package leo.decentralized.tonchat.data.repositories.security

interface SecurePrivateExecutionAndStorageRepository {
    suspend fun storePrivateKey(privateKey: ByteArray, publicKey: ByteArray,pass:String): Result<Boolean>
    fun deletePrivateKey(pass:String): Result<Boolean>
    fun signMessage(message: String,pass:String): Result<ByteArray>
    fun verifySignature(signature: ByteArray, message: String, publicKey: ByteArray,pass:String): Result<Boolean>
    fun getPublicKey(pass:String): Result<ByteArray>
    fun encryptMessage(message: String,pass:String): Result<String>
    fun decryptMessage(encryptedMessage: String,pass:String): Result<String>
}