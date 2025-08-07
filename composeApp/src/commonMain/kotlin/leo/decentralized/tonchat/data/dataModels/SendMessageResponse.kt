package leo.decentralized.tonchat.data.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageResponse(
    @SerialName("message")
    val message: String = "",
    @SerialName("time")
    val time: Long = 0
)