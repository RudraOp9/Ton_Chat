package leo.decentralized.tonchat.presentation.viewmodel.settings

import androidx.lifecycle.ViewModel
import leo.decentralized.tonchat.utils.PlatformLauncher

class AboutSettingsViewModel(private val platformLauncher: PlatformLauncher): ViewModel() {

    fun getHelpFromEmail(){
        platformLauncher.launchEmail("work.ieo@outlook.com","TON-Chat", "")//todo
    }

    fun openFAQUrl(){
        platformLauncher.launchUrl("https://www.github.com/rudraop9")//todo
    }

    fun openGithubUrl(){
        platformLauncher.launchUrl("https://www.github.com/rudraop9")//todo
    }

    fun openSupportUsUrl(){
        platformLauncher.launchUrl("https://www.github.com/rudraop9")//todo

    }



}