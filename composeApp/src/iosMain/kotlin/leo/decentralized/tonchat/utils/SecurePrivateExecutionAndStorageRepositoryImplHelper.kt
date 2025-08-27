package leo.decentralized.tonchat.utils

import leo.decentralized.tonchat.data.repositories.security.SecureStorageRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519

class SecurePrivateExecutionAndStorageRepositoryImplHelper: KoinComponent {
    private val secureStorageRepository : SecureStorageRepository by inject<SecureStorageRepository>()
    fun generateSharedKey(contactPublicKey:ByteArray){
        val edPrivateKey = PrivateKeyEd25519(secureStorageRepository.getPrivateKey().getOrThrow())
        val shared = edPrivateKey.sharedKey(PublicKeyEd25519(contactPublicKey))
    }
}