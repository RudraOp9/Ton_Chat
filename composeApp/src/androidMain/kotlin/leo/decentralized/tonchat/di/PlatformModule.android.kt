package leo.decentralized.tonchat.di

import leo.decentralized.tonchat.data.repositories.security.PasswordEncryptionRepository
import leo.decentralized.tonchat.data.repositories.security.PasswordEncryptionRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule = module {
    factory <PasswordEncryptionRepository>{ PasswordEncryptionRepositoryImpl(get()) }
}