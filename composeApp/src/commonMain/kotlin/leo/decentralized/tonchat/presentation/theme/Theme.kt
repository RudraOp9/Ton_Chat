package leo.decentralized.tonchat.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = Color(0,152,234),
    onPrimary = Color.White,
    background = Color(30,35,55),
    surface = Color(23, 33, 43),
    surfaceContainer = Color(35, 46, 60),
    surfaceContainerLow = Color(30, 39, 50),
    surfaceContainerHighest = Color(99, 99, 102)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0,152,234),
    onPrimary = Color.White,
    background = Color(247,249,251),
    surface = Color(247,249,251),
    surfaceContainer = Color(241, 241, 241),
    surfaceContainerLow = Color(244, 244, 245),
    surfaceContainerHighest = Color(227, 226, 230)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TonDecentralizedChatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
/*    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.setBackgroundDrawable(ColorDrawable(colorScheme.surface.toArgb()))
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }*/
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}