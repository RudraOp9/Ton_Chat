package leo.decentralized.tonchat.data.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Url
import leo.decentralized.tonchat.data.dataModels.GetV4R2Address
import leo.decentralized.tonchat.utils.Result

class TonChatApiRepositoryImpl(private val httpClient: HttpClient): TonChatApiRepository{
    override suspend fun getWalletAddress(publicKey:String): Result<String> {
        try {
            val request = httpClient.get(url = Url("https://ton-decentralized-chat.vercel.app/api/ton/generateV4R2Wallet")){
                headers["key"] = publicKey
            }
            val response = request.body<GetV4R2Address>()
            print(request.bodyAsText())
            println(response.toString())
            return Result(true,response.address)
        }catch (e:Exception){
            return Result(false,error = e)
        }
    }

    override suspend fun generateToken(
        publicKey: String,
        address: String,
        signature: String
    ): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun newUser(
        publicKey: String,
        address: String,
        token: String
    ): Result<String> {
        TODO("Not yet implemented")
    }
}