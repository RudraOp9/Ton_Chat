package leo.decentralized.tonchat.domain.usecase

import io.ktor.utils.io.charsets.MalformedInputException
import leo.decentralized.tonchat.data.dataModels.GenerateWalletResult
import leo.decentralized.tonchat.data.repositories.network.tonChatApi.TonChatApiRepositoryImpl
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.utils.Effect
import org.ton.crypto.Ed25519
import org.ton.crypto.SecureRandom
import org.ton.mnemonic.Mnemonic

class TonWalletUseCase(
    private val tonChatApi: TonChatApiRepositoryImpl,
    private val secureStorage: SecureStorageRepository
) {
    fun isWalletPhrasesValid(secretKeys: List<String>): Boolean {
        return secretKeys.all { Mnemonic.bip39English().contains(it) }
    }

    fun isWalletPhraseValid(secretKey: String): Boolean {
        return Mnemonic.bip39English().contains(secretKey)
    }

    fun generateMnemonics(size: Int): List<String> {
        return Mnemonic.generate(random = SecureRandom, wordsCount = size).words.toList()
    }

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun generateWallet(secretKeys: List<String>): Effect<GenerateWalletResult> {
        try {
            if (isWalletPhrasesValid(secretKeys) && secretKeys.size in setOf(12, 24)) {
                val result = Mnemonic(secretKeys).toSeed()
                val privateKey = result.slice(0..31).toByteArray()
                val publicKey = Ed25519.publicKey(privateKey)

                val storedPrivateKey = secureStorage.storePrivateKey(privateKey)
                if(!storedPrivateKey.isSuccess) throw Exception(storedPrivateKey.exceptionOrNull())

                val userFriendlyAddress = tonChatApi.getWalletAddress(publicKey.toHexString())
                if (!userFriendlyAddress.success) throw userFriendlyAddress.error
                    ?: MalformedInputException(
                        message = userFriendlyAddress.result ?: "Something went wrong"
                    )
                return Effect(
                    true,
                    result = GenerateWalletResult(
                        privateKey,
                        publicKey.toHexString(),
                        userFriendlyAddress.result ?: ""
                    )
                )
            } else return Effect(false, error = MalformedInputException(message = "Invalid phrase"))
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun signMessage(message: String): Effect<String> {
        try {
            val privateKeyResult = secureStorage.getPrivateKey()
            return if (privateKeyResult.isSuccess) {
                val privateKey = privateKeyResult.getOrThrow()
                val signature = Ed25519.sign(privateKey, message.encodeToByteArray())
                Effect(true, result = signature.toHexString())
            } else {
                Effect(false, error = Exception(privateKeyResult.exceptionOrNull()))
            }
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun generateNewToken(
        address: String,
        signature: String
    ): Effect<String> {
        try {
            val publicKey = secureStorage.getPublicKey().getOrThrow().toHexString()
            println("request : $publicKey $address $signature")
            val result = tonChatApi.generateToken(
                publicKey = publicKey,
                address = address,
                signature = signature
            )
            return if (result.success) {
                Effect(true, result = result.result?.token)
            } else {
                Effect(false, error = result.error)
            }
        }catch (e: Exception){
            return Effect(false, error = e)
        }
        //todo save valid till
    }

}
