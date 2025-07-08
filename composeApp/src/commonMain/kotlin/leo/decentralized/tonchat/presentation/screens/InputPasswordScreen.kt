package leo.decentralized.tonchat.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.navigation.PassCode
import leo.decentralized.tonchat.navigation.Screens
import leo.decentralized.tonchat.presentation.viewmodel.InputPasswordViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InputPasswordScreen(navHost: NavHostController,passCode:PassCode){
    val enteredPin = remember { mutableStateListOf<Int>() }
    var firstPin by remember { mutableStateOf<String?>(null) }
    val maxPinLength = 4
    var isConfirming by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val vm : InputPasswordViewModel = koinViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = if (passCode.isNew && isConfirming) "Confirm Passcode" else if (passCode.isNew && !isConfirming) "Set Passcode" else "Enter Passcode",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (if (passCode.isNew && isConfirming) "Re-enter" else if (passCode.isNew && !isConfirming) "Create a" else "Enter your" )+" 4-digit passcode.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(40.dp))
            PinDots(enteredLength = enteredPin.size, maxLength = maxPinLength, isError = isError){
                isError = false
            }
        }
        NumericKeyboard(
            onNumberClick = { number ->
                if (enteredPin.size < maxPinLength) {
                    enteredPin.add(number)
                    if (enteredPin.size == maxPinLength) {
                        val pinString = enteredPin.joinToString("")
                        if (passCode.isNew) {
                            if (isConfirming) {
                                if (pinString == firstPin) {
                                    vm.savePassword(enteredPin.joinToString(""))
                                    navHost.navigate(passCode.goTo){
                                        popUpTo(passCode){
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    isError = true
                                    println("PINs do not match. Try again.")
                                    enteredPin.clear()
                                }
                            } else {
                                firstPin = pinString
                                isConfirming = true
                                enteredPin.clear()
                            }
                        } else {
                            //validate pin todo : max attempts
                            vm.checkPassAndContinue(enteredPin.joinToString("")).onSuccess { // Example: incorrect PIN
                                enteredPin.clear()
                                firstPin = null
                                //todo continue surfing
                            }.onFailure {
                                enteredPin.clear()
                                isError = true
                            }
                        }
                    }
                }
            },
            onBackspaceClick = {
                if (enteredPin.isNotEmpty()) {
                    enteredPin.removeLast()
                    isError = false // Reset error state on backspace
                }
            }
        )
    }
}



@Composable
fun PinDots(enteredLength: Int, maxLength: Int, isError: Boolean, onErrorFinished:()->Unit) {
    val scope = rememberCoroutineScope()
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(isError) {
        if (isError) {
            scope.launch {
                shakeOffset.animateTo(
                    targetValue = 20f,
                    animationSpec = repeatable(
                        iterations = 3,
                        animation = tween(durationMillis = 50, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                shakeOffset.animateTo(0f, animationSpec = tween(durationMillis = 50))
                onErrorFinished()

            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = shakeOffset.value.dp) // Apply shake animation
    ) {
        for (i in 0 until maxLength) {
            val dotColor = animateColorAsState(
                targetValue = if (isError) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f)
                else if (i < enteredLength) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                animationSpec = tween(durationMillis = if (isError) 0 else 300), label = "dot color" // Faster color change on error
            )
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(dotColor.value),
            )
        }
    }
}

@Composable
fun NumericKeyboard(
    onNumberClick: (Int) -> Unit,
    onBackspaceClick: () -> Unit
) {
    val buttons = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "<-")
    )
    val hapticFeedback = LocalHapticFeedback.current
    //todo  hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)

    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        buttons.forEach { row ->
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { buttonText ->
                    if (buttonText.isEmpty()) {
                        Spacer(modifier = Modifier.size(72.dp)) // Placeholder for empty space
                    } else {
                        KeyboardButton(
                            text = buttonText,
                            onClick = {
                                when (buttonText) {
                                    "<-" -> onBackspaceClick()
                                    else -> onNumberClick(buttonText.toInt())
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KeyboardButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .clickable(remember{
                MutableInteractionSource()},null, onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (text == "<-") {
            Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = "Backspace", tint = MaterialTheme.colorScheme.primary)
        } else {
            Text(text = text, fontSize = 28.sp, color = MaterialTheme.colorScheme.primary)
        }
    }
}
