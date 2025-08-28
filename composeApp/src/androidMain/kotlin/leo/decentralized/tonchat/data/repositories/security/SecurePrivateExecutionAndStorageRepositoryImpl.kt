package leo.decentralized.tonchat.data.repositories.security

import android.security.keystore.KeyProperties
import android.security.keystore.KeyProperties.PURPOSE_DECRYPT
import android.security.keystore.KeyProperties.PURPOSE_ENCRYPT
import android.security.keystore.KeyProtection
import io.ktor.util.hex
import io.ktor.utils.io.core.toByteArray
import org.bouncycastle.asn1.edec.EdECObjectIdentifiers
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.AlgorithmIdentifier
import org.bouncycastle.asn1.x509.BasicConstraints
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.generators.HKDFBytesGenerator
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.HKDFParameters
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import java.math.BigInteger
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyStore
import java.security.SecureRandom
import java.security.Security
import java.security.Signature
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Date
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

// todo bulk encrypt and decrypt..
class SecurePrivateExecutionAndStorageRepositoryImpl(
   private val secureStorageRepository: SecureStorageRepository
) : SecurePrivateExecutionAndStorageRepository {

    private fun getContactKey(contactAddress:String, contactPublicKey: ByteArray): SecretKey{
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        if (keyStore.containsAlias("$contactAddress-chat")){
            val entry = keyStore.getEntry("$contactAddress-chat",null) as KeyStore.SecretKeyEntry
            return entry.secretKey
        }else{
            val edPrivateKey = PrivateKeyEd25519(secureStorageRepository.getPrivateKey().getOrThrow())
            val shared = edPrivateKey.sharedKey(PublicKeyEd25519(contactPublicKey))
            val aesKeyBytes = ByteArray(32)
            HKDFBytesGenerator(SHA256Digest()).apply {
                init(HKDFParameters(shared, null, null))
                generateBytes(aesKeyBytes, 0, aesKeyBytes.size)
            }
            val secretKey = SecretKeySpec(aesKeyBytes, "AES")
            val entry = KeyStore.SecretKeyEntry(secretKey)
            keyStore.setEntry(
                "$contactAddress-chat", entry,
                KeyProtection.Builder(PURPOSE_ENCRYPT or PURPOSE_DECRYPT)
                    .setRandomizedEncryptionRequired(true)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
            return getContactKey(contactAddress,contactPublicKey)
        }

    }

    private fun removeAllContactKeys(){
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        keyStore.aliases().toList().forEach {
            if (it.contains("-chat")){
                keyStore.deleteEntry(it)
            }
        }
    }

    @Deprecated("Can't save ed25519 in keystore")
    @Override
    override suspend fun storePrivateKey(
        privateKey: ByteArray,
        publicKey: ByteArray,
    ): Result<Boolean> {
        try {
            val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE).apply {
                load(null)
            }
            val algorithmIdentifier = AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519)
            val pkcs8EncodedPrivateKey = org.bouncycastle.asn1.pkcs.PrivateKeyInfo(
                algorithmIdentifier,
                org.bouncycastle.asn1.DEROctetString(privateKey)
            ).encoded
            val ed : Ed25519PrivateKeyParameters = Ed25519PrivateKeyParameters(privateKey)
            val keyFactory = KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
            val priv = keyFactory.generatePrivate(PKCS8EncodedKeySpec(pkcs8EncodedPrivateKey))


            // Create SubjectPublicKeyInfo
            val subjectPublicKeyInfo = SubjectPublicKeyInfo(algorithmIdentifier, publicKey)
            // Encode to X.509 format
            val x509EncodedPublicKey = subjectPublicKeyInfo.encoded
            val pub = keyFactory.generatePublic(X509EncodedKeySpec(x509EncodedPublicKey))


            val keyPair = KeyPair(pub, priv)
            val cert = generateSelfSignedCertificate(keyPair)
            val privateKeyEntry = KeyStore.PrivateKeyEntry(priv, arrayOf(cert))
            keyStore.setEntry(
                "rudra",
                privateKeyEntry,
                KeyProtection.Builder(
                    KeyProperties.PURPOSE_SIGN
                            or KeyProperties.PURPOSE_VERIFY
                )
                    .build()
            ).apply {
                return Result.success(true)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    @Deprecated("Can't save ed25519 in keystore")
    override fun deletePrivateKey(): Result<Boolean> {
        TODO("Not yet implemented")
    }

    @Deprecated("Can't save ed25519 in keystore")
    override fun signMessage(
        message: String
    ): Result<ByteArray> {
        try {
            var keyStore: KeyStore = KeyStore.getInstance(KEYSTORE).apply {
                load(null)
            }
            val key = keyStore.getKey("rudra", null) as KeyStore.PrivateKeyEntry
            val signature = Signature.getInstance(ALGORITHM)
            signature.initSign(key.privateKey)
            signature.update(message.toByteArray())
            return Result.success(signature.sign())
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    @Deprecated("Can't save ed25519 in keystore")
    override fun verifySignature(
        signature: ByteArray,
        message: String,
        publicKey: ByteArray,
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }


    @Deprecated("Can't save ed25519 in keystore")
    override fun getPublicKey(): Result<ByteArray> {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun encryptMessage(
        message: String,
        contactAddress: String,
        contactPublicKey: String
    ): Result<String> {

        try {
            val key = getContactKey(contactAddress,hex(contactPublicKey))
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val encryptedMessage  = cipher.doFinal(message.toByteArray())
            println("tag length ${cipher.parameters.getParameterSpec(GCMParameterSpec::class.java).tLen}")
            return Result.success(
                "${hex(encryptedMessage)}:${hex(cipher.iv)}:${cipher.parameters.getParameterSpec(GCMParameterSpec::class.java).tLen}"
            )
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override fun decryptMessage(
        encryptedMessage: String,
        contactAddress:String,
        contactPublicKey: String
    ): Result<String> {
        val key = getContactKey(contactAddress,hex(contactPublicKey))
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key,
        GCMParameterSpec(encryptedMessage.split(":")[2].toInt(), hex(encryptedMessage.split(":")[1])))
        return Result.success(cipher.doFinal(hex(encryptedMessage.split(":")[0])).decodeToString())
    }

    override fun generateSharedKey(
        contactPublicKey: ByteArray,
    ): Result<ByteArray> {
        try {
            val privateKey = secureStorageRepository.getPrivateKey().getOrThrow()
            val ed25519SharedKey = PrivateKeyEd25519(privateKey).sharedKey(PublicKeyEd25519(contactPublicKey))
            return Result.success(ed25519SharedKey)
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    companion object {
        private const val ALGORITHM: String = "Ed25519" // Using Ed25519 for signing

        //   private val BLOCK_MODE: String = KeyProperties.BLOCK_MODE_ECB
        //   private val PADDING: String = KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1
        private const val KEYSTORE: String = "AndroidKeyStore"

        //  private val TRANSFORMATION: String = String.format("%S/%S/%S", ALGORITHM, BLOCK_MODE, PADDING)
        fun generateSelfSignedCertificate(keyPair: KeyPair): X509Certificate {
            Security.setProperty("crypto.policy", "unlimited")
            Security.addProvider(BouncyCastleProvider()) // Ensure BouncyCastle is registered
            val issuer = X500Name("CN=Decentralized Ton Chat")
            val subject = X500Name("CN=Message Protection")
            val serialNumber = BigInteger.valueOf(SecureRandom().nextLong())
            val publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.public.encoded)
            val certBuilder: X509v3CertificateBuilder = JcaX509v3CertificateBuilder(
                issuer,
                serialNumber,
                Date(System.currentTimeMillis()),
                Date(System.currentTimeMillis() * 2),
                subject,
                publicKeyInfo
            )
            certBuilder.addExtension(
                Extension.subjectKeyIdentifier,
                false,
                Extension.subjectKeyIdentifier
            )
            certBuilder.addExtension(
                Extension.authorityKeyIdentifier,
                false,
                Extension.authorityKeyIdentifier
            )
            certBuilder.addExtension(Extension.basicConstraints, true, BasicConstraints(true))
            val signer = JcaContentSignerBuilder(ALGORITHM)
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .build(keyPair.private)
            val certHolder = certBuilder.build(signer)
            return JcaX509CertificateConverter().getCertificate(certHolder)
        }
    }

}