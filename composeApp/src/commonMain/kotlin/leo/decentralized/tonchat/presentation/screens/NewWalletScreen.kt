package leo.decentralized.tonchat.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import leo.decentralized.tonchat.presentation.viewmodel.NewWalletViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun NewWalletScreen(
    navController: NavController
) {
    val vm: NewWalletViewModel = koinViewModel()
    val snackBarHostState = SnackbarHostState()
    LaunchedEffect(vm.snackBarText.value) {
        if (vm.snackBarText.value.isNotEmpty()) {
            snackBarHostState.showSnackbar(vm.snackBarText.value)
            vm.snackBarText.value = ""
        }
    }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current


    LoadingScreen(vm.isLoading.value, vm.isLoadingText.value, snackBarHostState) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (vm.showBackupAlert.value) {
                AlertDialog(onDismissRequest = {
                    vm.showBackupAlert.value = false
                }, confirmButton = {
                    Button(onClick = {
                        vm.showBackupAlert.value = false
                        vm.isCopied.value = true
                        vm.canContinue()
                    }, shape = RoundedCornerShape(40)) {
                        Text("Yes")
                    }

                }, dismissButton = {
                    OutlinedButton(onClick = {
                        vm.showBackupAlert.value = false
                    }, shape = RoundedCornerShape(40)) {
                        Text("Go back", color = MaterialTheme.colorScheme.onSurface)
                    }


                }, title = {
                    Text("Is backup created ?")
                }, text = {
                    Text("This is the last time you will see these phrases here, please save them somewhere safe. Go back, If you haven't copied yet.")
                })
            }

            DefaultScreen(
                screenName = "New Wallet", onBack = {
                    navController.popBackStack()
                }, secondaryButton = {
                    Text(
                        text = if (!vm.isCopied.value) "Copy" else "Continue",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(/*vertical = 8.dp,*/ horizontal = 4.dp)
                            .clickable(
                                interactionSource = null,
                                indication = ripple(color = MaterialTheme.colorScheme.primary),
                                role = Role.Button
                            ) {
                                if (vm.isCopied.value) {
                                    vm.canContinue()
                                } else vm.copyPhrases(clipboardManager)
                            }
                            .padding(vertical = 2.dp, horizontal = 8.dp)

                    )
                },
                postLazyContent = {

                }
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Backup secret phrases",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "To get access to your Chats, enter these secret words given to you, do not expose these phrases to anyone.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        SelectionContainer {
                            Text(
                                text = vm.userFriendlyAddress.value,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(bottom = 24.dp)
                            )
                        }
                    }
                }
                items(vm.secretKeys.value.size) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(0.5f).wrapContentWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "${it + 1}.",
                                modifier = Modifier.width(24.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.End,
                            )
                            Spacer(Modifier.padding(start = 8.dp))
                            SelectionContainer {
                                Text(
                                    text = vm.secretKeys.value[it],
                                    modifier = Modifier.width(100.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(bottom = 100.dp))
                }
            }
            Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        vm.canContinue()
                    },
                    modifier = Modifier.fillMaxWidth() ,
                    shape = RoundedCornerShape(40)
                ) {
                    Text(
                        text = "Continue",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

        }
    }
}