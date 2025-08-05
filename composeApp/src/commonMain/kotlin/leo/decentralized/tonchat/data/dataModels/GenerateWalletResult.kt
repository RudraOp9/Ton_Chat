package leo.decentralized.tonchat.data.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class GenerateWalletResult(
    val privateKey: ByteArray,
    val publicKeyHex: String,
    val userFriendlyAddress: String
)