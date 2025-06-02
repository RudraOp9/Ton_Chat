package leo.decentralized.tonchat.di

import leo.decentralized.tonchat.data.repositories.TonChatApiRepositoryImpl
import leo.decentralized.tonchat.domain.usecase.FormatStringUseCase
import leo.decentralized.tonchat.domain.usecase.TonWalletUseCase
import leo.decentralized.tonchat.presentation.viewmodel.ImportWalletViewModel
import leo.decentralized.tonchat.presentation.viewmodel.NewWalletViewModel
import leo.decentralized.tonchat.utils.networking.createHttpClient
import leo.decentralized.tonchat.utils.networking.httpClientEngine
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {


    //view models
    viewModel { ImportWalletViewModel(get(), get()) }
    viewModel { NewWalletViewModel(get()) }
    //use cases
    factory { TonWalletUseCase(get()) }
    factory { FormatStringUseCase() }

    single { createHttpClient(get()) }
    single { httpClientEngine() }

    single { TonChatApiRepositoryImpl(get()) }


}