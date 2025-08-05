package leo.decentralized.tonchat.data.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class CheckUserExistResponse(
    val message:String? = null,
    val exists:Boolean? = null
)
