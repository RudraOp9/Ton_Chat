package leo.decentralized.tonchat.utils

import androidx.compose.ui.platform.ClipEntry

expect suspend fun ClipEntry.getText(): String?
