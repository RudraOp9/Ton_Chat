package leo.decentralized.tonchat

import androidx.compose.ui.window.ComposeUIViewController
import leo.decentralized.tonchat.data.repositories.security.PasswordEncryptionRepository

import org.koin.dsl.module

fun MainViewController(getPasswordEncryptionRepositoryImpl: () -> PasswordEncryptionRepository) = ComposeUIViewController(
    configure = {
    }) {
    App(discardSplashScreen = {}, module = module {
        single<PasswordEncryptionRepository> { getPasswordEncryptionRepositoryImpl() }
    })
}