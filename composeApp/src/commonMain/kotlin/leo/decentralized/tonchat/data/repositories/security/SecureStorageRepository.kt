package leo.decentralized.tonchat.data.repositories.security

interface SecureStorageRepository {
     fun storeToken(token: String):Result<Boolean>
     fun getToken():Result<String>
     fun deleteToken():Result<Boolean>

     fun storePrivateKey(privateKey:ByteArray):Result<Boolean>
     fun getPrivateKey():Result<ByteArray>
     fun deletePrivateKey():Result<Boolean>

     fun storePublicKey(publicKey: ByteArray): Result<Boolean>
     fun getPublicKey(): Result<ByteArray>

     fun deletePublicKey(): Result<Boolean>

    fun storeUserFriendlyAddress(address: String): Result<Boolean>
    fun getUserFriendlyAddress(): Result<String>
    fun deleteUserFriendlyAddress(): Result<Boolean>


}