package leo.decentralized.tonchat.presentation.viewmodel.settings

import androidx.lifecycle.ViewModel
import leo.decentralized.tonchat.utils.PlatformLauncher

class AboutSettingsViewModel(private val platformLauncher: PlatformLauncher): ViewModel() {

    fun getHelpFromEmail(){
        platformLauncher.launchEmail("","", "")//todo
    }

    fun openFAQUrl(){
        platformLauncher.launchUrl("")//todo
    }

    fun openGithubUrl(){
        platformLauncher.launchUrl("")//todo
    }

    fun openSupportUsUrl(){
        platformLauncher.launchUrl("")//todo

    }



}