package leo.decentralized.tonchat.data.repositories

import com.russhwolf.settings.Settings
import org.koin.core.Koin


expect fun getSettings(context: Koin): Settings

