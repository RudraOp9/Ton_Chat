package leo.decentralized.tonchat.utils

import leo.decentralized.tonchat.data.dataModels.Password
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class PasswordHelper : KoinComponent {
    private val password : Password by inject<Password>()
    fun getPassword(): Password = password
}