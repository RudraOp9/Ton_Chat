package leo.decentralized.tonchat.data.repositories.network.userApi

import leo.decentralized.tonchat.utils.Effect

interface UserApiRepository {
    suspend fun newUser(publicKey: String, address: String): Effect<String>
    suspend fun checkUserExist(address: String): Effect<Boolean>
    suspend fun deleteAccount(): Result<Unit>
}