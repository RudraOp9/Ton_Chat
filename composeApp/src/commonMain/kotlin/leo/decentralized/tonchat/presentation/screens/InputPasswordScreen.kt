package leo.decentralized.tonchat.presentation.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.rememberTransition
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
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import leo.decentralized.tonchat.presentation.navigation.Screen
import leo.decentralized.tonchat.presentation.viewmodel.InputPasswordViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InputPasswordScreen(navHost: NavHostController, isNew: Boolean, goTo: String) {
    var firstPin by rememberSaveable { mutableStateOf("") }
    var isConfirming by rememberSaveable { mutableStateOf(false) }
    var isError by rememberSaveable { mutableStateOf(false) }
    val vm: InputPasswordViewModel = koinViewModel()
    val enteredPin by vm.enteredPin.collectAsState()

    val onErrorFinishedCallback = remember {
        {
            isError = false
        }
    }
    val onNumberClickCallback = remember {
        { number:Int ->
            if (enteredPin.length < 4) {
                vm.changePin(number)
            }
        }
    }
    val onBackspaceClickCallback = remember {
        {
            if (enteredPin.isNotEmpty()) {
                vm.backspace()
            }
        }
    }

    LaunchedEffect(enteredPin){
        if (enteredPin.length == 4) {
            delay(100) // let the animation finish
            val pinString = enteredPin
            if (isNew) {
                if (isConfirming) {
                    if (pinString == firstPin) {
                        vm.savePassword()
                        if (goTo != ""){
                            navHost.navigate(goTo) {
                                popUpTo(Screen.PassCode(isNew, goTo)) {
                                    inclusive = true
                                }
                            }
                        }else{
                            navHost.popBackStack()
                        }

                    } else {
                        isError = true
                        vm.clearPin()
                    }
                } else {
                    firstPin = pinString
                    isConfirming = true
                    vm.clearPin()
                }
            } else {
                //validate pin todo : max attempts
                vm.savePassword()
                vm.checkPassAndContinue()
                    .onSuccess { // Example: incorrect PIN
                        vm.clearPin()
                        firstPin = ""
                        navHost.navigate(goTo) {
                            popUpTo(Screen.PassCode(isNew, goTo)) {
                                inclusive = true
                            }
                        }
                    }.onFailure {
                        vm.clearPin()
                        isError = true
                    }
            }
        }
    }

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
                text = if (isNew && isConfirming) "Confirm Passcode" else if (isNew && !isConfirming) "Set Passcode" else "Enter Passcode",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (if (isNew && isConfirming) "Re-enter" else if (isNew && !isConfirming) "Create a" else "Enter your") + " 4-digit ",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(40.dp))
            PinDots(enteredLength = enteredPin.length, isError = isError,onErrorFinishedCallback)
        }
        NumericKeyboard(
            onNumberClick = onNumberClickCallback,
            onBackspaceClick = onBackspaceClickCallback
        )
    }
}

@Composable
fun PinDots(enteredLength: Int, isError: Boolean, onErrorFinished: () -> Unit) {
    val shakeOffset = animateFloatAsState(
        if (isError)20f else 0f,
        animationSpec = repeatable(iterations = 3, animation = tween(easing = LinearEasing, durationMillis = 50), repeatMode = RepeatMode.Reverse),
        finishedListener = {
            onErrorFinished()
        }
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.graphicsLayer {
            this.translationX = shakeOffset.value
        },
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(4) { index ->
            val dotColor = rememberTransition(
                MutableTransitionState(
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                ), ""
            )
            val anim = dotColor.animateColor {
                if (isError) MaterialTheme.colorScheme.errorContainer.copy(
                    alpha = 0.8f
                )
                else if (index < enteredLength) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            }
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .drawBehind {
                        drawRect(anim.value)
                    }
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
        buttons.forEach {  row ->
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
            .clickable(remember {
                MutableInteractionSource()
            }, null, onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (text == "<-") {
            Icon(
                Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "Backspace",
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Text(text = text, fontSize = 28.sp, color = MaterialTheme.colorScheme.primary)
        }
    }
}
