package leo.decentralized.tonchat.domain.usecase

import leo.decentralized.tonchat.data.dataModels.Contact
import leo.decentralized.tonchat.data.repositories.network.chatApi.ChatApiRepository
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.presentation.screens.home.ChatMessage

class ChatUseCase(
    private val chatApi: ChatApiRepository,
    private val secureStorage: SecureStorageRepository
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

    suspend fun getChatFor(address: String): Result<List<ChatMessage>> {
        try {
            val result = chatApi.getChatFor(address)
            val myAddress = secureStorage.getUserFriendlyAddress().getOrThrow()
            if (result.success) {
                val listOfChats: MutableList<ChatMessage> = mutableListOf()
                listOfChats.apply {
                    result.result?.chats?.let { chats ->
                        addAll(
                            chats.sortedBy { sortedChat ->
                                sortedChat.time
                            }.map { mapEntry ->
                                ChatMessage(mapEntry.message, mapEntry.by == myAddress)
                            }
                        )
                    }
                }
                return Result.success(listOfChats)
            } else {
                return Result.failure(result.error ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun sendMessage(message:String){
        
    }
}