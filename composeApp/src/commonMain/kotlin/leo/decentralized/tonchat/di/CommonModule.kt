package leo.decentralized.tonchat.di

import com.russhwolf.settings.Settings
import leo.decentralized.tonchat.data.dataModels.Password
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepositoryImpl
import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import leo.decentralized.tonchat.data.repositories.network.tonChatApi.TonChatApiRepositoryImpl
import leo.decentralized.tonchat.data.repositories.network.userApi.UserApiRepository
import leo.decentralized.tonchat.data.repositories.network.userApi.UserApiRepositoryImpl
import leo.decentralized.tonchat.data.repositories.getSettings
import leo.decentralized.tonchat.data.repositories.network.chatApi.ChatApiRepository
import leo.decentralized.tonchat.data.repositories.network.chatApi.ChatApiRepositoryImpl
import leo.decentralized.tonchat.domain.usecase.ChatUseCase
import leo.decentralized.tonchat.domain.usecase.FormatStringUseCase
import leo.decentralized.tonchat.domain.usecase.TonWalletUseCase
import leo.decentralized.tonchat.domain.usecase.UserUseCase
import leo.decentralized.tonchat.presentation.viewmodel.ChatViewModel
import leo.decentralized.tonchat.presentation.viewmodel.HomeViewModel
import leo.decentralized.tonchat.presentation.viewmodel.ImportWalletViewModel
import leo.decentralized.tonchat.presentation.viewmodel.InputPasswordViewModel
import leo.decentralized.tonchat.presentation.viewmodel.NewWalletViewModel
import leo.decentralized.tonchat.presentation.viewmodel.settings.SettingsViewModel
import leo.decentralized.tonchat.presentation.viewmodel.SplashScreenViewModel
import leo.decentralized.tonchat.presentation.viewmodel.settings.AboutSettingsViewModel
import leo.decentralized.tonchat.presentation.viewmodel.settings.AccountManageSettingsViewModel
import leo.decentralized.tonchat.presentation.viewmodel.settings.PrivacyAndSecuritySettingsViewModel
import leo.decentralized.tonchat.utils.networking.createHttpClient
import leo.decentralized.tonchat.utils.networking.httpClientEngine
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {

    //view models
    viewModel { ImportWalletViewModel(get(), get(),get(),get(),get()) }
    viewModel { NewWalletViewModel(get(),get(),get()) }
    viewModel { InputPasswordViewModel(get(),get()) }
    viewModel { HomeViewModel(get(),get()) }
    viewModel { SplashScreenViewModel(get()) }
    viewModel { ChatViewModel(get()) }
    viewModel { SettingsViewModel(get()) }

    viewModel { AccountManageSettingsViewModel(get(),get(),get(),get()) }
    viewModel { AboutSettingsViewModel(get()) }
    viewModel { PrivacyAndSecuritySettingsViewModel(get(),get()) }


    //use cases
    factory { TonWalletUseCase(get(),get()) }
    factory { FormatStringUseCase() }
    factory { UserUseCase(get(),get()) }
    factory { ChatUseCase(get(), get(),get()) }

    single { createHttpClient(get()) }
    single { httpClientEngine() }

    single {
        TonChatApiRepositoryImpl(get(),get()) }
    single <UserApiRepository>{
        UserApiRepositoryImpl(get(),get())}
    single <Settings> {
        getSettings(getKoin()) }
    single <SecureStorageRepository> {
        SecureStorageRepositoryImpl(get(),get()) }
    single <ChatApiRepository>{
        ChatApiRepositoryImpl(get(),get()) }
    single <Password> {
        Password() }


}