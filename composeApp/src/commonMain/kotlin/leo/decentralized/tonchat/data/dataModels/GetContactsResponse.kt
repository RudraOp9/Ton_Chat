package leo.decentralized.tonchat.data.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetContactsResponse(
    @SerialName("contacts")
    val contacts: List<Contact>? = null,
    @SerialName("message")
    val message: String? = null
)

@Serializable
data class Contact(
    @SerialName("contacts")
    val contacts: String
)

