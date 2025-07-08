package leo.decentralized.tonchat.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import leo.decentralized.tonchat.navigation.PassCode
import leo.decentralized.tonchat.navigation.Screens
import org.jetbrains.compose.resources.painterResource
import ton_decentralized_chat.composeapp.generated.resources.Res
import ton_decentralized_chat.composeapp.generated.resources.welcome_desktop
import ton_decentralized_chat.composeapp.generated.resources.welcome_mobile

@Composable
fun WelcomeScreen(navController: NavController) {
    val uriHandler = LocalUriHandler.current
    val window = LocalWindowInfo.current
    val density = LocalDensity.current
    val isDesktop = remember {
        derivedStateOf {
            with(density) {
                window.containerSize.width.toDp() > 600.dp
            }
        }
    }


    // Replace with actual painterResource id for mobile and desktop assets
    val backgroundPainter: Painter = if (isDesktop.value)
        painterResource(Res.drawable.welcome_desktop)
    else
        painterResource(Res.drawable.welcome_mobile)

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.BottomCenter,
            colorFilter = if (isSystemInDarkTheme()) {
                ColorFilter.lighting(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.background
                )
            } else {
                null
            },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Decentralized Ton Chat",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = AnnotatedString("Learn more about the history and roadmap on GitHub"),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.clickable(remember { MutableInteractionSource() }, null) {
                        uriHandler.openUri("https://github.com/ton-blockchain") // Replace with your repo URL
                    }
                )
            }
            Column {
                OutlinedButton(
                    onClick = {
                        navController.navigate(PassCode(true,Screens.ImportWallet.screen))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(40),
                ) {
                    Text("Import Wallet")
                }
                Button(
                    onClick = {
                        navController.navigate(PassCode(true,Screens.NewWallet.screen))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(40),
                ) {
                    Text("New wallet")
                }
            }
        }
    }
}
