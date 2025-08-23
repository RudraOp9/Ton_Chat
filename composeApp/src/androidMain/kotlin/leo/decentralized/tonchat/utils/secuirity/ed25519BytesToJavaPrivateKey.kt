package leo.decentralized.tonchat.utils.secuirity

import leo.decentralized.tonchat.utils.Effect
import org.bouncycastle.asn1.ASN1OctetString
import org.bouncycastle.asn1.DEROctetString
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.asn1.x509.AlgorithmIdentifier
import org.bouncycastle.internal.asn1.edec.EdECObjectIdentifiers
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.Security
import java.security.spec.PKCS8EncodedKeySpec

/**
 * This is an ASN.1 DER encoded structure
 * */
fun ed25519BytesToJavaPrivateKey(key : ByteArray): Effect<PrivateKey> {
    try {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastleProvider())
        }
        val algorithmIdentifier = AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519)
        val keyOctetString: ASN1OctetString = DEROctetString(key)
        val privateKeyInfo = PrivateKeyInfo(algorithmIdentifier, keyOctetString)
        val pkcs8EncodedPrivateKey = privateKeyInfo.encoded
        val keySpec = PKCS8EncodedKeySpec(pkcs8EncodedPrivateKey)
        val keyFactory = KeyFactory.getInstance("Ed25519", BouncyCastleProvider.PROVIDER_NAME)
        return Effect(
            success = true,
            result = keyFactory.generatePrivate(keySpec)
        )
    } catch (e: Exception) {
        return Effect(
            success = false,
            error = e
        )
    }
}