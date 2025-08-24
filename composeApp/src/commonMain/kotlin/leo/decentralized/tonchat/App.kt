package leo.decentralized.tonchat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.ktor.util.hex
import io.ktor.utils.io.core.toByteArray
import leo.decentralized.tonchat.di.appModule
import leo.decentralized.tonchat.presentation.navigation.NavHost
import leo.decentralized.tonchat.presentation.theme.TonDecentralizedChatTheme
import leo.decentralized.tonchat.presentation.viewmodel.SplashScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.KoinAppDeclaration
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.Ed25519
import org.ton.mnemonic.Mnemonic

@Composable
fun App(koinAppDeclaration: KoinAppDeclaration? = null, discardSplashScreen: () -> Unit) {
    KoinApplication(
        application = remember {{
            koinAppDeclaration?.invoke(this)
            modules(appModule)
        }}
    ) {
        val vm : SplashScreenViewModel = koinViewModel()
        LaunchedEffect(vm.isLoading.value){
            if(!vm.isLoading.value){
                discardSplashScreen()
            }
        }
        if (!vm.isLoading.value) {
            TonDecentralizedChatTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    NavHost(vm.screen.value)
                }
            }
        }
    }
}



@Composable
@Preview
fun LOL() {
    val btText = rememberSaveable{
        mutableStateOf("click")
    }
    Button({

        val keyArray =
            Mnemonic(("silent huge hazard believe bid outer hockey record smooth maple popular cool stairs myth tired inquiry believe awake ketchup horror viable clay divide average")
                .splitToSequence(" ").toList()).toSeed()
        val keyArray2 =
            Mnemonic(("blue include vital scheme found cactus accuse since animal noble recycle culture general latin wrap super sword episode lawsuit tunnel supply obtain harsh worth")
                .splitToSequence(" ").toList()).toSeed() //EQBg3rlccFWUaG1fHt_WLhFxsVvUn_136dIznbQfVm8k79dh
        val privateKey = keyArray.slice(0..31).toByteArray()
        val publicKey = Ed25519.publicKey(privateKey)

        val privateKey2 = keyArray2.slice(0..31).toByteArray()
        val publicKey2 = Ed25519.publicKey(privateKey2)

        val sharedKey =  PrivateKeyEd25519(privateKey).sharedKey(PublicKeyEd25519(publicKey2))
        val shareKey2 = PrivateKeyEd25519(privateKey2).sharedKey(PublicKeyEd25519(publicKey))

        val sharedPrivateKey = PrivateKeyEd25519(PrivateKeyEd25519(privateKey).sharedKey(PublicKeyEd25519(publicKey2)))
        val sharedPrivateKey2 = PrivateKeyEd25519(PrivateKeyEd25519(privateKey2).sharedKey(PublicKeyEd25519(publicKey)))

        val encryptedMsg = hex(sharedPrivateKey.publicKey().encrypt("test".toByteArray()))
        val decryptedMsg = sharedPrivateKey2.decrypt(hex(encryptedMsg))

        val text = """
 privateKey = ${hex(privateKey)}
            publicKey = ${hex(publicKey)}
            privateKey2 = ${hex(privateKey2)}
            publicKey2 = ${hex(publicKey2)}
            sharedKey = ${hex(sharedKey)}
            shareKey2 = ${hex(shareKey2)}
            encryptedMsg = $encryptedMsg
            decryptedMsg = ${decryptedMsg.decodeToString()}
        """.trimIndent()
        btText.value = text

    }, modifier = Modifier.fillMaxSize()){
        Text(btText.value)

    }
}