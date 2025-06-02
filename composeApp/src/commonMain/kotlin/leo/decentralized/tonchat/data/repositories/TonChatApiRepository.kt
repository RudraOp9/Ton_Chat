package leo.decentralized.tonchat.data.repositories

import leo.decentralized.tonchat.utils.Result

interface TonChatApiRepository {
    suspend fun getWalletAddress(publicKey: String): Result<String>

    suspend fun generateToken(publicKey: String, address: String, signature: String): Result<String>

    suspend fun newUser(publicKey: String, address: String,token:String): Result<String>

    //suspend fun getContacts(token: String,address: String): Result<String>




}