package leo.decentralized.tonchat.data.repositories.network.userApi

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import leo.decentralized.tonchat.data.dataModels.CheckUserExistResponse
import leo.decentralized.tonchat.data.dataModels.GenericMessageResponse
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.utils.Effect

class UserApiRepositoryImpl(
    private val httpClient: HttpClient,
    private val secureStorageRepository: SecureStorageRepository
): UserApiRepository {
    override suspend fun newUser(
        publicKey: String,
        address: String
    ): Effect<String> {
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/user/newUser")) {
                    headers["publickey"] = publicKey //todo fix typo in api
                    headers["address"] = address
                    secureStorageRepository.getToken().onSuccess {
                        headers["token"] = it
                    }.onFailure { e ->
                        return Effect(false, error = Exception(e.message))
                    }

                }
            val response = request.body<GenericMessageResponse>()

            return if (request.status.value in 200..299) {
                Effect(true, response.message)
            } else {
                Effect(false, error = Exception(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }

    override suspend fun checkUserExist(
        address: String
    ): Effect<Boolean> {
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/user/checkUserExist")) {
                    headers["address"] = address
                    secureStorageRepository.getToken().onSuccess {
                        headers["token"] = it
                    }.onFailure { e ->
                        return Effect(false, error = Exception(e.message))
                    }
                }
            val response = request.body<CheckUserExistResponse>()
            return if (request.status.value in 200..299) {
                Effect(true, response.exists)
            } else {
                Effect(false, error = Exception(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/user/checkUserExist")) {
                    secureStorageRepository.getToken().onSuccess {
                        headers["token"] = it
                    }.onFailure { e ->
                        return Result.failure(Exception(e.message))
                    }
                }
            return if (request.status.value in 200..299) {
                Result.success(Unit)
            } else {
                val response = request.body<GenericMessageResponse>()
                Result.failure(Exception( response.message?:"Something went wrong"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}