package leo.decentralized.tonchat.utils

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(enable: Boolean, onBack: ()-> Unit):Unit