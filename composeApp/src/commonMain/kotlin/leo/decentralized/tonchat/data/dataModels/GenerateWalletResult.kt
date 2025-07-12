package leo.decentralized.tonchat.data.dataModels

data class GenerateWalletResult(
    val privateKey: ByteArray,
    val publicKeyHex: String,
    val userFriendlyAddress: String
)