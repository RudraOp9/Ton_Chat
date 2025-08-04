package leo.decentralized.tonchat.data.repositories.network.tonChatApi

import leo.decentralized.tonchat.data.dataModels.GenToken
import leo.decentralized.tonchat.utils.Effect

interface TonChatApiRepository {
    suspend fun getWalletAddress(publicKey: String): Effect<String>

    suspend fun generateToken(publicKey: String, address: String, signature: String): Effect<GenToken>


    //suspend fun getContacts(token: String,address: String): Result<String>




}