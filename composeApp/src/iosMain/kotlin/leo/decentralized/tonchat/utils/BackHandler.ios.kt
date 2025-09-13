package leo.decentralized.tonchat.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun BackHandler(enable: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enable, onBack = onBack)
}