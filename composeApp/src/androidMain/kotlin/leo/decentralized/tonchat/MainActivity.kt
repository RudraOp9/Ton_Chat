package leo.decentralized.tonchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import leo.decentralized.tonchat.presentation.viewmodel.SplashScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.logger.Level

class MainActivity : ComponentActivity() {
    var isLoading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{isLoading}
        enableEdgeToEdge()
        setContent {
            App(koinAppDeclaration = {
                androidLogger(Level.DEBUG)
                androidContext(this@MainActivity)
            }){
                isLoading = false
            }
        }
    }
}

