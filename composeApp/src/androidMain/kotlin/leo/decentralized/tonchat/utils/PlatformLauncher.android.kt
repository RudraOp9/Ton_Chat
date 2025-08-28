package leo.decentralized.tonchat.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import leo.decentralized.tonchat.Platform
import androidx.core.net.toUri

class PlatformLauncherImpl(private val context: Context): PlatformLauncher{
    override fun launchEmail(email: String, subject: String, text: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }

        context.startActivity(intent)

    }

    override fun launchUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = url.toUri()
        }
        context.startActivity(intent)
    }
}