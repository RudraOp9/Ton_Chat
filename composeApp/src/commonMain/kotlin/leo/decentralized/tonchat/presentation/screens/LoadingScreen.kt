package leo.decentralized.tonchat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoadingScreen(
    isLoading: Boolean,
    supportingText: String = "",
    snackbarHostState: SnackbarHostState,
    content: @Composable () -> Unit
) {
    SnackbarHost(snackbarHostState, modifier = Modifier.imePadding())

        Box(modifier = Modifier.fillMaxSize()) {
            content()
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .clickable(null, null, enabled = false) {}
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                ) {
                    Card(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            if (supportingText.isNotEmpty()) {
                                Text(
                                    text = supportingText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

        }

}