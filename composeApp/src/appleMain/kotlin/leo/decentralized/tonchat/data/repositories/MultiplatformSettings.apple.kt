package leo.decentralized.tonchat.data.repositories

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import org.koin.core.Koin

@OptIn(ExperimentalSettingsImplementation::class)
actual fun getSettings(context: Koin): Settings {
    return KeychainSettings()
}