package leo.decentralized.tonchat.data.repositories.network.userApi

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Url
import leo.decentralized.tonchat.data.dataModels.GenericMessageResponse
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.utils.Result

class UserApiRepositoryImpl(
    private val httpClient: HttpClient,
    private val secureStorageRepository: SecureStorageRepository
): UserApiRepository {
    override suspend fun newUser(
        token : String,
        publicKey: String,
        address: String
    ): Result<String> {
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/user/newUser")) {
                    headers["publickey"] = publicKey //todo fix typo in api
                    headers["address"] = address
                    secureStorageRepository.getToken().onSuccess {
                        headers["token"] = it
                    }.onFailure { e ->
                        return Result(false, error = Exception(e.message))
                    }

                }
            val response = request.body<GenericMessageResponse>()
            print(request.bodyAsText())
            println(response.toString())

            return if (request.status.value in 200..299) {
                Result(true, response.message)
            } else {
                Result(false, error = Exception(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception) {
            return Result(false, error = e)
        }
    }
}