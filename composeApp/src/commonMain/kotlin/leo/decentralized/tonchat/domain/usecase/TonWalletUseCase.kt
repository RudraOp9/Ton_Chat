package leo.decentralized.tonchat.domain.usecase

import leo.decentralized.tonchat.data.repositories.TonChatApiRepositoryImpl
import leo.decentralized.tonchat.utils.Result
import org.ton.api.exception.TonException
import org.ton.crypto.Ed25519
import org.ton.crypto.hex
import org.ton.mnemonic.Mnemonic


data class GenerateWalletResult(
    val privateKey: ByteArray,
    val publicKeyHex: String,
    val userFriendlyAddress:String
)

class TonWalletUseCase(private val tonChatApi : TonChatApiRepositoryImpl){
    fun isWalletPhrasesValid(secretKeys: List<String>): Boolean {
        return secretKeys.all { Mnemonic.mnemonicWords().contains(it) }
    }

    fun isWalletPhraseValid(secretKey : String):Boolean{
        return Mnemonic.mnemonicWords().contains(secretKey)
    }

    suspend fun generateMnemonics(size: Int): List<String> {
        return Mnemonic.generate(wordCount = size)
    }

    suspend fun generateWallet(secretKeys: List<String>): Result<GenerateWalletResult> {
        try {
            if (isWalletPhrasesValid(secretKeys) && secretKeys.size in setOf(12, 24)) {
                val privateKey = Mnemonic.toSeed(secretKeys)
                val publicKey = hex(Ed25519.publicKey(privateKey))
                val userFriendlyAddress = tonChatApi.getWalletAddress(publicKey)
                if (userFriendlyAddress.success == false) throw userFriendlyAddress.error?: TonException(message = "Something went wrong", code =1)
                return Result(true, result = GenerateWalletResult(privateKey, publicKey, userFriendlyAddress.result?:""))
            } else return Result(false, error =  TonException(code = 1, message = "Invalid phrase"))
        } catch (e: Exception) {
            return Result(false, error = e)
        }

    }

    fun signMessage(privateKey: ByteArray, message: String): Result<String> {
        try {
            val result = Ed25519.sign(privateKey, message.encodeToByteArray())
            return Result(true, result = hex(result))
        } catch (e: Exception) {
            return Result(false, error = e)
        }
    }

}