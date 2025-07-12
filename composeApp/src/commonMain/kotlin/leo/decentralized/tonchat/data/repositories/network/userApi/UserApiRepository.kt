package leo.decentralized.tonchat.data.repositories.network.userApi

import leo.decentralized.tonchat.utils.Result

interface UserApiRepository {
    suspend fun newUser(publicKey: String, address: String): Result<String>
    suspend fun checkUserExist(address: String): Result<Boolean>
}