package leo.decentralized.tonchat.di

import leo.decentralized.tonchat.data.repositories.security.PasswordEncryptionRepository
import leo.decentralized.tonchat.data.repositories.security.PasswordEncryptionRepositoryImpl
import leo.decentralized.tonchat.data.repositories.security.SecurePrivateExecutionAndStorageRepository
import leo.decentralized.tonchat.data.repositories.security.SecurePrivateExecutionAndStorageRepositoryImpl
import leo.decentralized.tonchat.utils.PlatformLauncher
import leo.decentralized.tonchat.utils.PlatformLauncherImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule = module {
    factory <PasswordEncryptionRepository>{
        PasswordEncryptionRepositoryImpl(get()) }
    single < SecurePrivateExecutionAndStorageRepository>{
        SecurePrivateExecutionAndStorageRepositoryImpl(get())
    }
    factory <PlatformLauncher>{
        PlatformLauncherImpl(get())
    }

}