package leo.decentralized.tonchat.navigation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import ton_decentralized_chat.composeapp.generated.resources.Res
import ton_decentralized_chat.composeapp.generated.resources.*

enum class Screens(val screen: String) {
    Welcome(screen = Res.string.welcome.key),
    ImportWallet(screen = Res.string.import_wallet.key),
    NewWallet(screen = Res.string.create_new_wallet.key),
    ChatScreen(screen = Res.string.chat_screen.key),
    HomeScreen(screen = Res.string.home_screen.key)
}

//interface Screen

@Immutable
@Serializable
data class PassCode(val isNew: Boolean, val goTo: String)