package leo.decentralized.tonchat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
    postLazyContent : @Composable () -> Unit = {},
    content: LazyListScope.() -> Unit = {},
) {
    /*    var toolbarHeightPx = 0f
        val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
        var crrheight by remember { mutableIntStateOf(0) }
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    val newOffset = toolbarOffsetHeightPx.value + delta
                    toolbarOffsetHeightPx.value = newOffset.coerceIn(-(toolbarHeightPx), 0f)
                    return Offset.Zero
                } } }*/

    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)

        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .displayCutoutPadding().systemBarsPadding(),
                contentAlignment = Alignment.Center
            ) {
                if (onBack != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
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
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
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
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp).padding(top = 4.dp).navigationBarsPadding()) {
            content()
        }
        postLazyContent()
        /*Box(modifier = Modifier.nestedScroll(nestedScrollConnection)) {
            LazyColumn(contentPadding = PaddingValues(top = with(LocalDensity.current) {
                crrheight.toDp().plus(
                    toolbarOffsetHeightPx.value.toDp(),
                )
            })) {
                content()
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        toolbarHeightPx = it.size.height.toFloat()
                    }.offset {
                        IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt())
                    }.onSizeChanged {
                        toolbarHeightPx = it.height.toFloat()
                        crrheight = it.height
                    }
                    .background(color = MaterialTheme.colorScheme.surface)) {

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable(
                            interactionSource = null, indication = null, onClick = onBack
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = screenName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.scrim
                    )
                }
            }

        }*/
    }
}
