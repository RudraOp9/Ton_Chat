package leo.decentralized.tonchat.data.repositories.network.userApi

import leo.decentralized.tonchat.utils.Result

interface UserApiRepository {
    suspend fun newUser(token:String, publicKey: String, address: String): Result<String>
}