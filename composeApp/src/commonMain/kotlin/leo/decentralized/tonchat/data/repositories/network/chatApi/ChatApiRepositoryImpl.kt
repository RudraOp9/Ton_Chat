package leo.decentralized.tonchat.data.repositories.network.chatApi

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.Url
import leo.decentralized.tonchat.data.dataModels.GenericMessageResponse
import leo.decentralized.tonchat.data.dataModels.GetChatResponse
import leo.decentralized.tonchat.data.dataModels.GetContactsResponse
import leo.decentralized.tonchat.data.dataModels.SendMessageResponse
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

            response.contacts?.forEach {
                println(it.address)
            }

            return if (request.status.value in 200..299) {
                Effect(true, response)
            } else {
                Effect(false, error = Exception(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }

    override suspend fun newContact(address: String): Effect<GenericMessageResponse> {
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/chat/newChat")) {
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
                    headers["contactaddress"] = address
                }
            val response = request.body<GenericMessageResponse>()
            return if (request.status.value in 200..299) {
                Effect(true, response)
            } else {
                Effect(false, error = Exception(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }

    override suspend fun getChatFor(chatId: String): Effect<GetChatResponse> {
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/chat/getChats")) {
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
                    headers["contact"] = chatId
                }
            val response = request.body<GetChatResponse>()
            response.chats.forEach {
                println(it.message)
            }
            return if (request.status.value in 200..299) {
                Effect(true, response)
            } else {
                Effect(false, error = Exception(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }

    override suspend fun sendMessage(message: String, to: String): Effect<SendMessageResponse> {
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/chat/sendMessage")) {
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
                    headers["message"] = message
                    headers["sendto"] = to
                }
            val response = request.body<SendMessageResponse>()
            return if (request.status.value in 200..299) {
                Effect(true, response)
            } else {
                Effect(false, error = Exception(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }

    override suspend fun wipeChats(): Effect<Unit> {
        //   TODO("Not yet implemented")
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/chat/wipeChats")) {
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
            val response = request.body<GenericMessageResponse>()
            return if (request.status.value in 200..299) {
                Effect(true, Unit)
            } else {
                Effect(false, error = Exception(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }

    override suspend fun deleteChat(): Effect<Unit> {
        //   TODO("Not yet implemented")
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/chat/deleteChat")) {
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
            val response = request.body<GenericMessageResponse>()
            return if (request.status.value in 200..299) {
                Effect(true, Unit)
            } else {
                Effect(false, error = Exception(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }
}