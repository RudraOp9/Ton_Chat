package leo.decentralized.tonchat.data.repositories

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.Koin
import java.util.prefs.Preferences

actual fun getSettings(context: Koin): Settings {
    val pref = Preferences.userRoot().node("decentralized/tonChat")
    return PreferencesSettings(pref)
}