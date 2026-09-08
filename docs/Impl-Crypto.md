# Before start

`AES-128-CFB8`, `RSA1024` and `SHA1` has been implemented in `libmc-protocol-context` module, it uses
`cryptography-kotlin`(and its platform based provider)

> All impl example below are based on Java's built-in security & cryptography API

# AES-128-CFB8

```kotlin
class JavaAesCipher(sharedKey: ByteArray) : NetworkCipher {
    private val encryptCipher = Cipher.getInstance("AES/CFB8/NoPadding").apply {
        init(Cipher.ENCRYPT_MODE, SecretKeySpec(sharedKey, "AES"), IvParameterSpec(sharedKey))
    }

    private val decryptCipher = Cipher.getInstance("AES/CFB8/NoPadding").apply {
        init(Cipher.DECRYPT_MODE, SecretKeySpec(sharedKey, "AES"), IvParameterSpec(sharedKey))
    }

    override fun encrypt(buffer: ByteArray, offset: Int, length: Int) {
        encryptCipher.update(buffer, offset, length, buffer, offset)
    }

    override fun decrypt(buffer: ByteArray, offset: Int, length: Int) {
        decryptCipher.update(buffer, offset, length, buffer, offset)
    }
}
```

# RSA1024

```kotlin
private fun encrypt(publicKeyBytes: ByteArray, data: ByteArray): ByteArray {
    val keySpec = X509EncodedKeySpec(publicKeyBytes)
    val keyFactory = KeyFactory.getInstance("RSA")
    val publicKey = keyFactory.generatePublic(keySpec)
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    return cipher.doFinal(data)
}

public val CustomProtocolContext: ProtocolContextBuilder.() -> Unit = {
    rsaEncryptor = RSA1024Encryptor { key: ByteArray, data: ByteArray -> encrypt(key, data) }
}
```

# SHA1

```kotlin
private fun minecraftServerIdHash(serverId: String, secretKey: ByteArray, publicKey: ByteArray): String {
    val serverIdBytes: ByteArray = serverId.encodeToByteArray()
    for (b in serverIdBytes) require((b.toInt() and 0xFF) <= 0x7F) { "serverId contains non-US-ASCII character" }
    val data = ByteArray(serverIdBytes.size + secretKey.size + publicKey.size)
    System.arraycopy(serverIdBytes, 0, data, 0, serverIdBytes.size)
    System.arraycopy(secretKey, 0, data, serverIdBytes.size, secretKey.size)
    System.arraycopy(publicKey, 0, data, serverIdBytes.size + secretKey.size, publicKey.size)
    val digest = MessageDigest.getInstance("SHA-1")
    val hash = digest.digest(data)
    return BigInteger(hash).toString(16)
}

public val CustomProtocolContext: ProtocolContextBuilder.() -> Unit = {
    sha1Hasher = Sha1Hasher { serverId: String, secretKey: ByteArray, publicKey: ByteArray ->
        minecraftServerIdHash(serverId, secretKey, publicKey)
    }
}
```