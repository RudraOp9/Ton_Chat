package leo.decentralized.tonchat.data.repositories.network.chatApi

import leo.decentralized.tonchat.data.dataModels.GetContactsResponse
import leo.decentralized.tonchat.utils.Effect

interface ChatApiRepository {
    suspend fun getContacts(): Effect<GetContactsResponse>
    // suspend fun getMessages(chatId: String): List<Message>

}