package leo.decentralized.tonchat

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

fun main() = application() {
    val windowState = rememberWindowState(width = 400.dp, height = 800.dp)
    Window(
        onCloseRequest = ::exitApplication,
       // alwaysOnTop = true,
        state = windowState,
        title = "Ton Decentralized Chat"
    ) {
        val localDensity = LocalDensity.current
        this.window.minimumSize = Dimension(
            with(localDensity) { 400.dp.toPx() }.toInt(),
            with(localDensity) { 800.dp.toPx() }.toInt()
        )
        App()
    }
}