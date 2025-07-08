package leo.decentralized.tonchat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DefaultScreen(
    modifier: Modifier = Modifier,
    screenName: String,
    onBack: (() -> Unit)? = null,
    secondaryButton: @Composable () -> Unit = {},
    postLazyContent: @Composable () -> Unit = {},
    content: LazyListScope.() -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)

        ) {
            println(
                "window insets : status bar : ${WindowInsets.statusBars.asPaddingValues().calculateTopPadding()}, cutout : ${WindowInsets.displayCutout.asPaddingValues().calculateTopPadding()}, union : ${
                    WindowInsets.statusBars.union(
                        WindowInsets.displayCutout
                    ).asPaddingValues().calculateTopPadding()
                }"
            )
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                    .windowInsetsPadding(WindowInsets.statusBars.union(WindowInsets.displayCutout)),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (onBack != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.clickable(
                                interactionSource = null, indication = null, onClick = onBack
                            )
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = screenName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    secondaryButton()
                }
            }

        }
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 4.dp)
                .navigationBarsPadding()
        ) {
            content()
        }
        postLazyContent()
    }
}
