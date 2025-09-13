package leo.decentralized.tonchat.domain.usecase

import leo.decentralized.tonchat.data.dataModels.Contact
import leo.decentralized.tonchat.data.dataModels.GetContactsResponse
import leo.decentralized.tonchat.data.repositories.network.chatApi.ChatApiRepository
import leo.decentralized.tonchat.data.repositories.network.userApi.UserApiRepository
import leo.decentralized.tonchat.data.repositories.security.SecurePrivateExecutionAndStorageRepository
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.presentation.screens.ChatMessage

class ChatUseCase(
    private val chatApi: ChatApiRepository,
    private val secureStorage: SecureStorageRepository,
    private val securePrivateExecutionAndStorage: SecurePrivateExecutionAndStorageRepository,
) {
    suspend fun getContacts(): Result<GetContactsResponse> {
        val result = chatApi.getContacts()
        return if (result.success) {
            Result.success(result.result!!)
        } else {
            Result.failure(result.error ?: Exception("Unknown error"))
        }
    }

    suspend fun newContact(address: String): Result<Boolean> {
        val result = chatApi.newContact(address)
        return if (result.success) {
            Result.success(true)
        } else {
            Result.failure(result.error ?: Exception("Unknown error"))
        }
    }

    suspend fun getChatFor(contactAddress: String, contactPublicKey:String, onNewMessage: (Result<ChatMessage>) -> Unit): Result<Unit> {
        try {
            val result = chatApi.getChatFor(contactAddress)
            val myAddress = secureStorage.getUserFriendlyAddress().getOrThrow()
            if (result.success) {
                result.result?.chats?.let { chats ->
                    chats.forEach { mapEntry ->
                        securePrivateExecutionAndStorage.decryptMessage(
                            mapEntry.message,
                            contactAddress,
                            contactPublicKey
                        ).onSuccess {
                            onNewMessage(Result.success(ChatMessage(it, mapEntry.by == myAddress)))
                        }.onFailure {
                            onNewMessage(Result.failure(it))
                        }
                    }
                }
                return Result.success(Unit)
            } else {
                return Result.failure(result.error ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun sendMessage(message:String, to:String, contactPublicKey: String): Result<Long>{
        val encryptedMessage = securePrivateExecutionAndStorage.encryptMessage(
            message = message,
            contactAddress = to,
            contactPublicKey = contactPublicKey
        ).getOrElse {
            return Result.failure(it)
        }
        val result = chatApi.sendMessage(message = encryptedMessage, to = to)
        return if (result.success) {
            Result.success(result.result?.time?:0L)
        } else {
            Result.failure(result.error ?: Exception("Unknown error"))
        }
    }

    suspend fun wipeChats():Result<Unit>{
        val result = chatApi.wipeChats()
        return if (result.success) {
            Result.success(Unit)
        } else {
            Result.failure(result.error ?: Exception("Unknown error"))
        }
    }

     suspend fun deleteAccount():Result<Unit>{
         val result = chatApi.deleteChat()
         return if (result.success) {
             Result.success(Unit)
         } else {
             Result.failure(result.error ?: Exception("Unknown error"))
         }
    }
}