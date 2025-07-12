package leo.decentralized.tonchat.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import leo.decentralized.tonchat.presentation.viewmodel.ImportWalletViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ImportWalletScreen(
    navController: NavController
) {
    val vm: ImportWalletViewModel = koinViewModel()
    val focusManager = LocalFocusManager.current
    val clipboard: ClipboardManager = LocalClipboardManager.current

    val snackBarHost = SnackbarHostState()
    LaunchedEffect(vm.snackBarText.value) {
        if (vm.snackBarText.value.isNotEmpty()) {
            snackBarHost.showSnackbar(vm.snackBarText.value)
            vm.snackBarText.value = ""
        }
    }
    LoadingScreen(
        isLoading = vm.isLoading.value,
        supportingText = vm.isLoadingText.value,
        snackbarHostState = snackBarHost
    ) {
        DefaultScreen(screenName = "Import Wallet", onBack = {
            navController.popBackStack()
        }, secondaryButton = {
            Text(
                text = if (!vm.isSecretKeysValid.value) "Paste" else "Continue",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(/*vertical = 8.dp*/)
                    .clickable(
                        interactionSource = null,
                        indication = ripple(color = MaterialTheme.colorScheme.primary),
                        role = Role.Button
                    ) {
                        if (vm.isSecretKeysValid.value) {
                            vm.importWallet()
                        } else vm.pasteSecretKeys(clipboard)
                    }
                    .padding(vertical = 2.dp)

            )

        }) {

            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Enter secret phrase",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "To get access to your Chats, enter the secret words given to you when you created your wallet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            onClick = { vm.toggleSecretKeys24() },
                            shape = RoundedCornerShape(
                                topEndPercent = 0,
                                topStartPercent = 40,
                                bottomEndPercent = 0,
                                bottomStartPercent = 40
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = animateColorAsState(
                                    targetValue = if (vm.isSecretKeys24.value) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerLow,
                                    animationSpec = tween(),
                                    label = "24WordsContainerColor"
                                ).value,
                                contentColor = animateColorAsState(
                                    targetValue = if (vm.isSecretKeys24.value) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surfaceContainerHighest,
                                    animationSpec = tween(),
                                    label = "24WordsContentColor"
                                ).value
                            ),
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            Text("24 words")
                        }
                        // Spacer(modifier = Modifier.padding(start = 0.5.dp))
                        OutlinedButton(
                            onClick = { vm.toggleSecretKeys24() },
                            shape = RoundedCornerShape(
                                topEndPercent = 40,
                                topStartPercent = 0,
                                bottomEndPercent = 40,
                                bottomStartPercent = 0
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = animateColorAsState(
                                    targetValue = if (!vm.isSecretKeys24.value) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerLow,
                                    label = "12WordsContainerColor"
                                ).value,
                                contentColor = animateColorAsState(
                                    targetValue = if (!vm.isSecretKeys24.value) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surfaceContainerHighest,
                                    animationSpec = tween(),
                                    label = "24WordsContentColor"
                                ).value
                            ),
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            Text("12 words")
                        }
                    }
                }
            }

            items(if (vm.isSecretKeys24.value) 24 else 12) { index ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    var isError by rememberSaveable {
                        mutableStateOf(false)
                    }

                    OutlinedTextField(
                        value = vm.secretKeys.value[index],
                        onValueChange = { newValue ->
                            if (newValue.trim().contains(" ")) {
                                vm.pasteSecretKeys(keyIndex = index, clipData = newValue)
                                return@OutlinedTextField
                            }
                            if (!newValue.contains(Regex("[\\W\\d]"))) {
                                vm.secretKeys.value = vm.secretKeys.value.toMutableList().also {
                                    it[index] = newValue
                                }
                            }
                        },
                        prefix = {
                            Text(
                                text = "${index + 1} : ",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        isError = vm.secretKeys.value[index].contains(regex = Regex("[\\W\\d]")) || isError,
                        modifier = Modifier
                            .padding(vertical = 4.dp).onFocusChanged {
                                if (!it.hasFocus) {
                                    isError = if (vm.secretKeys.value[index].isNotEmpty()) {
                                        !vm.checkWalletPhrase(vm.secretKeys.value[index])
                                    } else {
                                        false
                                    }
                                }
                            }.onKeyEvent {
                                if (it.key == Key.Backspace) {
                                    if (vm.secretKeys.value[index].isEmpty()) {
                                        focusManager.moveFocus(FocusDirection.Up)
                                        true
                                    } else false
                                } else {
                                    false
                                }
                            },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions().copy(
                            imeAction = ImeAction.Next,
                            autoCorrectEnabled = true,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        })
                    )
                }
            }
            item {
                Button(
                    enabled = vm.isSecretKeysValid.value,
                    onClick = {
                        vm.importWallet()
                    },
                    modifier = Modifier.fillMaxWidth(),
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

