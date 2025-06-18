package leo.decentralized.tonchat.domain.usecase

import io.ktor.util.hex
import io.ktor.utils.io.charsets.MalformedInputException
import kotlinx.io.IOException
import leo.decentralized.tonchat.data.repositories.TonChatApiRepositoryImpl
import leo.decentralized.tonchat.utils.Result
import org.ton.crypto.Ed25519
import org.ton.crypto.SecureRandom
import org.ton.mnemonic.Mnemonic


data class GenerateWalletResult(
    val privateKey: ByteArray,
    val publicKeyHex: String,
    val userFriendlyAddress:String
)

class TonWalletUseCase(private val tonChatApi : TonChatApiRepositoryImpl){
    fun isWalletPhrasesValid(secretKeys: List<String>): Boolean {
        return secretKeys.all { Mnemonic.bip39English().contains(it) }
    }

    fun isWalletPhraseValid(secretKey : String):Boolean{
        return Mnemonic.bip39English().contains(secretKey)
    }

    suspend fun generateMnemonics(size: Int): List<String> {
        return Mnemonic.generate(random = SecureRandom, wordsCount = size).words.toList()
    }

    suspend fun generateWallet(secretKeys: List<String>): Result<GenerateWalletResult> {
        try {
            if (isWalletPhrasesValid(secretKeys) && secretKeys.size in setOf(12, 24)) {
                val privateKey = Mnemonic(secretKeys).toSeed()
                val publicKey = hex(Ed25519.publicKey(privateKey.slice(32..63).toByteArray()))
                val userFriendlyAddress = tonChatApi.getWalletAddress(publicKey)
                if (userFriendlyAddress.success == false) throw userFriendlyAddress.error?: MalformedInputException(message = userFriendlyAddress.result?:"Something went wrong")
                return Result(true, result = GenerateWalletResult(privateKey, publicKey, userFriendlyAddress.result?:""))
            } else return Result(false, error = MalformedInputException(message = "Invalid phrase"))
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