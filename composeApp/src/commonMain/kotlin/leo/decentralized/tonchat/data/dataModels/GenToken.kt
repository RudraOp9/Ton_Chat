package leo.decentralized.tonchat.data.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class GenToken(
    val message: String? = null,
    val token: String? = null,
    val validTill: Long? = null
)