TON CHAT ( WIP )

An secure chat application intigrated with TON security and features.

Application uses ED25519 and XC25519 for encryption/decryption/signature.


Tech Stack :

- Kotlin Multiplatform
- Compose Multiplatform
- Material UI
- MVVM Architecture
- Language : Kotlin, Swift
- Crypto : ED25519, XC25519, AES


Targeted platforms : Android, IOS, Desktop


Flow : 

1. Saving Keys to Device : Generate ED25519 KeyPair from Seed -> Encrypt Private Key with AES ( Generated using PIN ) -> Save to Device ( EncryptedSharedPrefrences / KeyChain )

2. Authentication with server :  Sign Challenge code with Private key which will be verified by server for Token.
code could be : { address:UserFriendlyAddress, timestamp: Local_Time }

3. Chat Exchange : 
                      First Ed25519 has to be converted to XC25519
                      User1 and User2 will share their public Keys
                      User1 PrivateKey + User2 PublicKey = Shared Key
                      SharedKey -> AES Key -> KeyStore/Secure enclave
                      Shared Key will be same for both users
                      Chats can be decrypted/encrypted with this AES key



