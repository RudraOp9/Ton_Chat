package leo.decentralized.tonchat.utils

interface PlatformLauncher {
    //todo ios
    fun launchEmail(email: String, subject: String, text: String)
    fun launchUrl(url: String)
}