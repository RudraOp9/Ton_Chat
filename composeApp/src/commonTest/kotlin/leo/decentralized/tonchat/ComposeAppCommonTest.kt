package leo.decentralized.tonchat


import io.ktor.utils.io.core.toByteArray
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.contract.wallet.WalletV3R2Contract
import org.ton.crypto.Ed25519
import org.ton.crypto.hex
import org.ton.mnemonic.Mnemonic
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppCommonTest {

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun generateWallet() {
        //val key = PrivateKeyEd25519()

        // val key = Mnemonic.(("conduct sentence hat family fade runway mystery banner garden comfort journey loud window rapid cabbage leave join slam scatter dune keen swim genre random keen diet error leaf isolate win yard assume tuition yellow raise credit artwork surprise thrive service cushion nose observe praise matter benefit develop").splitToSequence(" ").toList())
        val keyArray = Mnemonic.toSeed(
            ("silent huge hazard believe bid outer hockey record smooth maple popular cool stairs myth tired inquiry believe awake ketchup horror viable clay divide average").splitToSequence(
                " "
            ).toList()
        )
        val publicKey = Ed25519.publicKey(keyArray)
        val hexAddr = hex(publicKey)

    //    val wallet = WalletV3R2Contract.address(PrivateKeyEd25519.decode(keyArray)).toString(userFriendly = true)
     //   val wallet = WalletV3R2Contract.address(PrivateKeyEd25519.Companion.decode(keyArray), 0)
      //      .toString(userFriendly = true)

        val address = Base64.UrlSafe.encode(hexAddr.toByteArray())
        println("key array  : ${keyArray.joinToString()}")
        println("public key : ${publicKey.joinToString()}")
        println("address : $address")
        println("hex address : $hexAddr")
      //  println("wallet address : $wallet")


        /*WalletV3R2Contract.address()
        val secretKey = key.toPrivateKey()
        val publicKey = secretKey.publicKey()*/
        /*        val address = Base64.UrlSafe.decode(publicKey.toByteArray())
                println("public key : "+publicKey.toByteArray().joinToString()+"\n secretKey : "+ secretKey.toByteArray().joinToString())
                println("address : "+address.joinToString())*/

        assertEquals(3, 1 + 2)
    }

}