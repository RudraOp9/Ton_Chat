package leo.decentralized.tonchat.data.repositories.network.chatApi

import leo.decentralized.tonchat.data.dataModels.GenericMessageResponse
import leo.decentralized.tonchat.data.dataModels.GetChatResponse
import leo.decentralized.tonchat.data.dataModels.GetContactsResponse
import leo.decentralized.tonchat.data.dataModels.SendMessageResponse
import leo.decentralized.tonchat.utils.Effect

interface ChatApiRepository {
    suspend fun getContacts(): Effect<GetContactsResponse>
    suspend fun newContact(address : String): Effect<GenericMessageResponse>
    suspend fun getChatFor(chatId: String): Effect<GetChatResponse>
    suspend fun sendMessage(message: String, to: String): Effect<SendMessageResponse>
    suspend fun wipeChats(): Effect<Unit>
    suspend fun deleteChat(): Effect<Unit>

}