package leo.decentralized.tonchat.utils

import androidx.compose.ui.platform.ClipEntry

actual suspend fun ClipEntry.getText(): String? {
        val itemCount = clipData.itemCount
        for (i in 0 ..< itemCount) {
            val item = clipData.getItemAt(i)
            val text = item?.text
            if (text != null) {
                return text.toString()
            }
        }
        return null
}
