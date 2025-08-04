package leo.decentralized.tonchat.data.repositories.network.tonChatApi

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Url
import leo.decentralized.tonchat.data.dataModels.GenToken
import leo.decentralized.tonchat.data.dataModels.GetV4R2Address
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.utils.Effect

class TonChatApiRepositoryImpl(
    private val httpClient: HttpClient,
    private val secureStorageRepository: SecureStorageRepository
) : TonChatApiRepository {
    override suspend fun getWalletAddress(publicKey: String): Effect<String> {
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/ton/generateV4R2Wallet")) {
                    headers["key"] = publicKey
                }
            val response = request.body<GetV4R2Address>()
            print(request.bodyAsText())
            println(response.toString())
            return Effect(true, response.address)
        } catch (e: Exception) {
            return Effect(false, error = e)
        }
    }

    override suspend fun generateToken(
        publicKey: String,
        address: String,
        signature: String
    ): Effect<GenToken> {
        try {
            val request =
                httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/auth/genToken")) {
                    headers["secretkey"] = publicKey //todo fix typo in api
                    headers["address"] = address
                    headers["signature"] = signature
                }
            val response = request.body<GenToken>()
            print(request.bodyAsText())
            println(response.toString())

            return if (request.status.value in 200..299) {
                Effect(true, response)
            } else {
                Effect(false, error = Exception(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception) {
            return Effect(false, error = e)
        }

    }
}