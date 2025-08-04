package leo.decentralized.tonchat.data.repositories.network.chatApi

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import leo.decentralized.tonchat.data.dataModels.GetContactsResponse
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.utils.Effect

class ChatApiRepositoryImpl(
    private val httpClient: HttpClient,
    private val secureStorageRepository: SecureStorageRepository
):ChatApiRepository {
    override suspend fun getContacts(): Effect<GetContactsResponse> {
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/user/getContacts")) {
                    secureStorageRepository.getToken().onSuccess {
                        headers["token"] = it
                    }.onFailure { e ->
                        return Effect(false, error = Exception(e.message))
                    }
                    secureStorageRepository.getUserFriendlyAddress().onSuccess {
                        headers["address"] = it
                    }.onFailure { e ->
                        return Effect(false, error = Exception(e.message))
                    }
                }
            val response = request.body<GetContactsResponse>()
            return if (request.status.value in 200..299) {
                Effect(true, response)
            } else {
                Effect(false, error = Exception(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }
}