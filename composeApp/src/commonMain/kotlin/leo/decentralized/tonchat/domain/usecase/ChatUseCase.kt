package leo.decentralized.tonchat.domain.usecase

import leo.decentralized.tonchat.data.dataModels.Contact
import leo.decentralized.tonchat.data.repositories.network.chatApi.ChatApiRepository
import leo.decentralized.tonchat.data.repositories.network.tonChatApi.TonChatApiRepositoryImpl
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.utils.Effect

class ChatUseCase(
    private val chatApi: ChatApiRepository,
    private val secureStorage: SecureStorageRepository
) {
    suspend fun getContacts(): Result<List<Contact>> {
        val result = chatApi.getContacts()
        return if (result.success){
            Result.success(result.result?.contacts.orEmpty())
        }else{
            Result.failure(result.error?:Exception("Unknown error"))
        }
    }
}