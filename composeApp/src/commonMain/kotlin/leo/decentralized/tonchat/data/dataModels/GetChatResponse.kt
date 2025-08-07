package leo.decentralized.tonchat.data.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetChatResponse(
    @SerialName("chats")
    val chats: List<Chat> = listOf(),
    @SerialName("message")
    val message: String = ""
)

@Serializable
data class Chat(
    @SerialName("by")
    val by: String = "",
    @SerialName("message")
    val message: String = "",
    @SerialName("time")
    val time: Long = 0
)