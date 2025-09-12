package leo.decentralized.tonchat.data.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetContactsResponse(
    @SerialName("contacts")
    val contacts: List<Contact>? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("spamContacts")
    val spamContacts: List<Contact>? = null
)

@Serializable
data class Contact(
    @SerialName("address")
    val address: String,
    @SerialName("public_Key")
    val publicKey: String
)

