package leo.decentralized.tonchat.data.repositories

interface SecureStorageRepository {
     fun storeToken(token: String):Result<Boolean>
     fun getToken():Result<String?>
     fun deleteToken():Result<Boolean>

     fun storePrivateKey(privateKey:ByteArray):Result<Boolean>
     fun getPrivateKey(address:String):Result<ByteArray>
     fun deletePrivateKey(address:String):Result<Boolean>


}