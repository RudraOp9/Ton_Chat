package leo.decentralized.tonchat.data.repositories.security

import android.security.keystore.KeyProperties
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
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
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


class SecurePrivateExecutionAndStorageRepositoryImpl(
   private val secureStorageRepository: SecureStorageRepository
) : SecurePrivateExecutionAndStorageRepository {
    // private var cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
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
                            or KeyProperties.PURPOSE_DECRYPT
                            or KeyProperties.PURPOSE_ENCRYPT
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
        contactPublicKey: ByteArray
    ): Result<String> {
        val sharedKey = generateSharedKey(contactPublicKey).getOrThrow()
        val encMessage = PrivateKeyEd25519(sharedKey).publicKey().encrypt(message.toByteArray())
        return Result.success(hex(encMessage))
    }

    override fun decryptMessage(
        encryptedMessage: String,
        contactPublicKey: ByteArray
    ): Result<String> {
        val sharedKey = generateSharedKey(contactPublicKey).getOrThrow()
        val decryptedMessage = PrivateKeyEd25519(sharedKey).decrypt(hex(encryptedMessage))
        return Result.success(decryptedMessage.decodeToString())
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
        private const val ALGORITHM: String = KeyProperties.KEY_ALGORITHM_EC

        //   private val BLOCK_MODE: String = KeyProperties.BLOCK_MODE_ECB
        //   private val PADDING: String = KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1
        private const val KEYSTORE: String = "AndroidKeyStore"

        //  private val TRANSFORMATION: String = String.format("%S/%S/%S", ALGORITHM, BLOCK_MODE, PADDING)
        fun generateSelfSignedCertificate(keyPair: KeyPair): X509Certificate {
            Security.setProperty("crypto.policy", "unlimited")
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