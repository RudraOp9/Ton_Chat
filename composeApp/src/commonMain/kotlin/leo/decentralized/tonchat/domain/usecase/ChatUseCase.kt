package leo.decentralized.tonchat.domain.usecase

import io.ktor.util.hex
import io.ktor.utils.io.core.toByteArray
import leo.decentralized.tonchat.data.dataModels.Contact
import leo.decentralized.tonchat.data.repositories.network.chatApi.ChatApiRepository
import leo.decentralized.tonchat.data.repositories.security.SecurePrivateExecutionAndStorageRepository
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.presentation.screens.home.ChatMessage

class ChatUseCase(
    private val chatApi: ChatApiRepository,
    private val secureStorage: SecureStorageRepository,
    private val securePrivateExecutionAndStorage: SecurePrivateExecutionAndStorageRepository,
) {
    suspend fun getContacts(): Result<List<Contact>> {
        val result = chatApi.getContacts()
        return if (result.success) {
            Result.success(result.result?.contacts.orEmpty())
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

    suspend fun getChatFor(address: String,contactPublicAddress:String,onNewMessage: (Result<ChatMessage>) -> Unit): Result<Unit> {
        println("contactPublicAddress : $contactPublicAddress")
        try {
            val result = chatApi.getChatFor(address)
            val myAddress = secureStorage.getUserFriendlyAddress().getOrThrow()
            if (result.success) {
                val listOfChats: MutableList<ChatMessage> = mutableListOf()
                listOfChats.apply {
                    result.result?.chats?.let { chats ->
                        chats.forEach { mapEntry ->
                            securePrivateExecutionAndStorage.decryptMessage(
                                mapEntry.message,
                                hex(contactPublicAddress)
                            ).onSuccess {
                                onNewMessage(Result.success(ChatMessage(it,mapEntry.by == myAddress)))
                            }.onFailure {
                                onNewMessage(Result.failure(it))
                            }
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

    suspend fun sendMessage(message:String, to:String, contactPublicAddress: String): Result<Long>{
        val encryptedMessage = securePrivateExecutionAndStorage.encryptMessage(
            message = message,
            contactPublicKey = hex(contactPublicAddress)
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
}