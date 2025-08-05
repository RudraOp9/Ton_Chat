package leo.decentralized.tonchat.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import androidx.savedstate.read
import leo.decentralized.tonchat.presentation.screens.ImportWalletScreen
import leo.decentralized.tonchat.presentation.screens.InputPasswordScreen
import leo.decentralized.tonchat.presentation.screens.NewWalletScreen
import leo.decentralized.tonchat.presentation.screens.WelcomeScreen
import leo.decentralized.tonchat.presentation.screens.home.ChatScreen
import leo.decentralized.tonchat.presentation.screens.home.HomeScreen

@Composable
fun NavHost(defaultScreen:String) {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = defaultScreen,
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {

        composable(Screens.Welcome.screen) {
            WelcomeScreen(navHostController)
        }

        composable<PassCode> {
            val passCode = it.toRoute<PassCode>()
            InputPasswordScreen(navHostController,passCode.isNew,passCode.goTo)
        }

        composable(Screens.ImportWallet.screen) {
            //todo erase data
            ImportWalletScreen(navHostController)
        }

        composable(Screens.NewWallet.screen) {
            //todo erase data
            NewWalletScreen(navHostController)
        }

        composable(Screens.HomeScreen.screen) {
            HomeScreen(navHostController)
        }

        composable (Screens.ChatScreen.screen ){
            ChatScreen(navHostController)
        }



    }
}