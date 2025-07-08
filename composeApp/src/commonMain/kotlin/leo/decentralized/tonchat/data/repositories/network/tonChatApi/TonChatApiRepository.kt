package leo.decentralized.tonchat.data.repositories.network.tonChatApi

import leo.decentralized.tonchat.data.dataModels.GenToken
import leo.decentralized.tonchat.utils.Result

interface TonChatApiRepository {
    suspend fun getWalletAddress(publicKey: String): Result<String>

    suspend fun generateToken(publicKey: String, address: String, signature: String): Result<GenToken>


    //suspend fun getContacts(token: String,address: String): Result<String>




}