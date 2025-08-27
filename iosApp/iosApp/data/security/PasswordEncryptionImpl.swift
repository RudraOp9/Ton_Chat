//
// Created by rudra on 23/08/25.
//

import Foundation
import ComposeApp
import CommonCrypto
import CryptoKit

enum CryptoError: Error {
    case keyDerivationFailed
    case encryptionFailed(String)
    case decryptionFailed(String)
    case dataConversionFailed(String)
    case invalidInput(String)
}

class PasswordEncryptionImpl: PasswordEncryptionRepository {
    
    var password: Password = PasswordHelper().getPassword()
    private let saltString = "TON_DECENTRALIZED_CHAT"
    private let iterationCount = 65536
    private let keyLengthInBits = 256
    
    init(){
        
    }
    func generateKey() throws -> SymmetricKey{
        let passwordData = password.password!.data(using: .utf8)!
        let saltData = saltString.data(using: .utf8)!
        var derivedKey = [UInt8](repeating: 0, count: keyLengthInBits / 8)
        let result = CCKeyDerivationPBKDF(CCPBKDFAlgorithm(kCCPBKDF2), (passwordData as NSData).bytes, passwordData.count, (saltData as NSData).bytes, saltData.count, CCPseudoRandomAlgorithm(kCCPRFHmacAlgSHA256), UInt32(iterationCount), &derivedKey, derivedKey.count)
        
        guard result == kCCSuccess else {
            throw CryptoError.keyDerivationFailed
        }

        return SymmetricKey(data: Data(derivedKey))
    }
    func encrypt(stringToEncrypt: String) -> Effect<NSString> {
        do{
            let sealBox = try AES.GCM.seal(stringToEncrypt.data(using: .utf8)!, using: generateKey())
            return Effect(success: true,result: (sealBox.combined!.base64EncodedString()) as NSString, error: nil)
        }catch{
            return Effect(success: false, result: nil, error: KotlinException(message: error.localizedDescription))
        }
    }
    
    func decrypt(stringToDecrypt: String) -> Effect<NSString> {
        do{
            let sealedBox = try AES.GCM.SealedBox(combined: Data(base64Encoded: stringToDecrypt)!)
            let decryptedData = try AES.GCM.open(sealedBox, using: generateKey())
            return Effect(success: true, result: String(data: decryptedData, encoding: .utf8) as NSString?,error: nil)
        }catch{
            return Effect(success: false, result: nil, error: KotlinException(message: error.localizedDescription))
        }
    }
}
