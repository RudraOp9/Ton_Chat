package leo.decentralized.tonchat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import leo.decentralized.tonchat.di.appModule
import leo.decentralized.tonchat.navigation.NavHost
import leo.decentralized.tonchat.presentation.screens.ImportWalletScreen
import leo.decentralized.tonchat.presentation.screens.NewWalletScreen
import leo.decentralized.tonchat.presentation.theme.TonDecentralizedChatTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import ton_decentralized_chat.composeapp.generated.resources.Res
import ton_decentralized_chat.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App(koinAppDeclaration: KoinAppDeclaration? = null) {
    KoinApplication(
        application = {
            koinAppDeclaration?.invoke(this)
            modules(appModule)
        }
    ) {
        TonDecentralizedChatTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                NavHost()
            }
        }
    }
}