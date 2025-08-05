package leo.decentralized.tonchat.data.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class GenericMessageResponse(
    val message:String? = null
)
