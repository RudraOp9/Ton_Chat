package leo.decentralized.tonchat


import io.ktor.util.hex
import io.ktor.utils.io.core.toByteArray
import kotlinx.io.Buffer
import kotlinx.io.bytestring.toHexString
import kotlinx.io.readString
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.contract.wallet.WalletV3R2Contract
import org.ton.contract.wallet.WalletV4R2Contract
import org.ton.crypto.DecryptorEd25519
import org.ton.crypto.Ed25519
import org.ton.crypto.Ed25519.publicKey
import org.ton.mnemonic.Mnemonic
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppCommonTest {

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @OptIn(ExperimentalEncodingApi::class, ExperimentalStdlibApi::class)
    @Test
    fun generateWallet() {
        //val key = PrivateKeyEd25519()
        val keyArray =
            Mnemonic(("silent huge hazard believe bid outer hockey record smooth maple popular cool stairs myth tired inquiry believe awake ketchup horror viable clay divide average")
                .splitToSequence(" ").toList()).toSeed()

        val privateHex  = hex(keyArray)
        val publicKey = publicKey(keyArray.slice(32..63).toByteArray())
        val publicKey1 = keyArray.slice(32..63).toByteArray()
        val hexAddr = hex(publicKey)

      //  val wallet = WalletV4R2Contract.address(private, 0).toString(userFriendly = true,urlSafe = true, bounceable = false)
     //   val wallet = WalletV3R2Contract.address(PrivateKeyEd25519.Companion.decode(keyArray), 0)
      //      .toString(userFriendly = true)
        val key = PrivateKeyEd25519()
        val sign = key.sign("hello".toByteArray())
        val result = key.publicKey().verify(message = "hello".toByteArray(), signature = "aa9f9a143e80846e69cec2107047cf6b089f5871544048ed1b719332e33e30ed3bc3e969da588f4ad6f76c822ce161e0a7227e685c0280544c40fde763b74f02".hexToByteArray())
        println(result)

      //  val sign2 = Ed25519.sign(private1,"hello".toByteArray())
     //   val result2 = Ed25519.verify(key.publicKey().key.toHexString().hexToByteArray(),"hello".toByteArray(),sign2)
     //   println(result2)

       // val address = Base64.UrlSafe.encode(hexAddr.toByteArray())
        val private1 = keyArray.sliceArray(0..31)
        println(private1.toHexString())
        val pub = Ed25519.publicKey(private1)
        println("pub : "+ pub.toHexString())
        val cig = Ed25519.sign(private1,"EQDhR_xH7im4EsrUIXJsjRSQZ0af-tWKf-yXWX3qqWT5BiBn".toByteArray())
        println("cig ${cig.toHexString()}")
        val ver = Ed25519.verify(pub, "hello".encodeToByteArray(), "7688d4462479f1902bea0addc7c387454dbe3533bff9353343eb0680a7a6b3b4d36027772f90fc444a48984bfac444f97f287627877a31a27de9e1ee1a47890e".hexToByteArray())
        println(ver)


        println("key array  : ${keyArray.joinToString()}")
        println("private hex : ${hex(keyArray.sliceArray(0..31))}                   ${hex(keyArray.sliceArray(32..63))}")
        println("ed private publickey : "+key.publicKey().key.toHexString())
        println("ed private  : "+hex(key.key.toByteArray()))
        //println("public key : ${publicKey.joinToString()}")
       // println("address : $address")
        println("hex address : $hexAddr")
  //      println("wallet address : $wallet")


        /*WalletV3R2Contract.address()
        val secretKey = key.toPrivateKey()
        val publicKey = secretKey.publicKey()*/
        /*        val address = Base64.UrlSafe.decode(publicKey.toByteArray())
                println("public key : "+publicKey.toByteArray().joinToString()+"\n secretKey : "+ secretKey.toByteArray().joinToString())
                println("address : "+address.joinToString())*/

        assertEquals(3, 1 + 2)
    }

}