package leo.decentralized.tonchat.domain.usecase

import io.ktor.utils.io.charsets.MalformedInputException
import leo.decentralized.tonchat.data.repositories.network.tonChatApi.TonChatApiRepositoryImpl
import leo.decentralized.tonchat.data.repositories.security.SecurePrivateExecutionAndStorageRepository
import leo.decentralized.tonchat.utils.Result
import org.ton.crypto.Ed25519
import org.ton.crypto.SecureRandom
import org.ton.mnemonic.Mnemonic


data class GenerateWalletResult(
    val privateKey: ByteArray,
    val publicKeyHex: String,
    val userFriendlyAddress: String
)

class TonWalletUseCase(
    private val tonChatApi: TonChatApiRepositoryImpl,
    private val secureExec: SecurePrivateExecutionAndStorageRepository
) {
    fun isWalletPhrasesValid(secretKeys: List<String>): Boolean {
        return secretKeys.all { Mnemonic.bip39English().contains(it) }
    }

    fun isWalletPhraseValid(secretKey: String): Boolean {
        return Mnemonic.bip39English().contains(secretKey)
    }

    suspend fun generateMnemonics(size: Int): List<String> {
        return Mnemonic.generate(random = SecureRandom, wordsCount = size).words.toList()
    }

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun generateWallet(secretKeys: List<String>): Result<GenerateWalletResult> {
        try {
            if (isWalletPhrasesValid(secretKeys) && secretKeys.size in setOf(12, 24)) {
                val result = Mnemonic(secretKeys).toSeed()
                val privateKey = result.slice(0..31).toByteArray()
                val publicKey = Ed25519.publicKey(privateKey)
                secureExec.storePrivateKey(privateKey,publicKey,"1234").onSuccess {
                    println("result2 $it")
                }.onFailure {
                    println("result2 "+it.message)
                    it.printStackTrace()
                }
                val userFriendlyAddress = tonChatApi.getWalletAddress(publicKey.toHexString())
                if (userFriendlyAddress.success == false) throw userFriendlyAddress.error
                    ?: MalformedInputException(
                        message = userFriendlyAddress.result ?: "Something went wrong"
                    )
                return Result(
                    true,
                    result = GenerateWalletResult(
                        privateKey,
                        publicKey.toHexString(),
                        userFriendlyAddress.result ?: ""
                    )
                )
            } else return Result(false, error = MalformedInputException(message = "Invalid phrase"))
        } catch (e: Exception) {
            return Result(false, error = e)
        }

    }

    @OptIn(ExperimentalStdlibApi::class)
    fun signMessage(privateKey: ByteArray, message: String): Result<String> {
        try {
            val result = Ed25519.sign(privateKey, message.encodeToByteArray())
            println("result " +result.toHexString())
            secureExec.signMessage(message,"1234").onSuccess {
                println("result2 "+it.toHexString())
            }.onFailure {
                println("result2 "+it.message)
                it.printStackTrace()
            }

            return Result(true, result = result.toHexString())
        } catch (e: Exception) {
            return Result(false, error = e)
        }
    }

    suspend fun generateNewToken(
        publicKeyHex: String,
        address: String,
        signature: String
    ): Result<String> {
        println("request : $publicKeyHex $address $signature")

        val result = tonChatApi.generateToken(
            publicKey = publicKeyHex,
            address = address,
            signature = signature
        )
        if (result.success) {
            return Result(true, result = result.result?.token)
        } else {
            return Result(false, error = result.error)
        }
        //todo save valid till
    }

}