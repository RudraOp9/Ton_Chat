package leo.decentralized.tonchat.utils.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


fun createHttpClient(engine: HttpClientEngine):HttpClient{
    return HttpClient(engine){
        install(ContentNegotiation){
            json(json = Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging){
            this.level = LogLevel.ALL
        }
    }
}