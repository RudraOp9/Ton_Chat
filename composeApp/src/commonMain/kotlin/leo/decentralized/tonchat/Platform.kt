package leo.decentralized.tonchat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform