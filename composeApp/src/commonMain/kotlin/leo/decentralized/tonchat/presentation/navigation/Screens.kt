package leo.decentralized.tonchat.presentation.navigation

import kotlinx.serialization.Serializable
import ton_decentralized_chat.composeapp.generated.resources.Res
import ton_decentralized_chat.composeapp.generated.resources.*

enum class Screens(val screen: String) {
    Welcome(screen = Res.string.welcome.key),
    ImportWallet(screen = Res.string.import_wallet.key),
    NewWallet(screen = Res.string.create_new_wallet.key),
    HomeScreen(screen = Res.string.home_screen.key)
}

//interface Screen
sealed class Screen{
    @Serializable
    data class ChatScreen(val contactAddress:String, val contactPublicKey: String):Screen()
    @Serializable
    data class PassCode(val isNew: Boolean, val goTo: String):Screen()
}
