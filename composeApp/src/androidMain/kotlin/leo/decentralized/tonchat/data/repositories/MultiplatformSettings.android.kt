package leo.decentralized.tonchat.data.repositories

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import leo.decentralized.tonchat.data.dataModels.Password
import org.koin.core.Koin


@RequiresApi(Build.VERSION_CODES.P)
actual fun getSettings(context: Koin): Settings {
    val applicationContext: Context = context.get()
    try {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val encSharedPref = EncryptedSharedPreferences.create(
            "settings",
            masterKeyAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return SharedPreferencesSettings(encSharedPref)
    } catch (e: Exception) {
        Toast.makeText(
            applicationContext,
            "The app cannot function on this device because : " + e.message,
            Toast.LENGTH_LONG
        ).show()
        throw e
    }
}