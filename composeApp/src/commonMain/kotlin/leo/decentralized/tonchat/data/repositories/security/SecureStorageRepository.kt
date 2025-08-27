package leo.decentralized.tonchat.data.repositories.security

import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow

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

    fun storeUserFriendlyAddress(userFriendlyAddress: String): Result<Boolean>
    fun getUserFriendlyAddress(): Result<String>
    fun deleteUserFriendlyAddress(): Result<Boolean>

    fun setLoggedIn(loggedIn: Boolean): Result<Boolean>
    fun getLoggedIn(): Result<Boolean>

    var currentTheme: MutableStateFlow<Int>

    fun refreshTheme()
    fun setTheme(theme:Int): Result<Unit>


}