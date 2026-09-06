* **AES-128-CFB8** (`NetworkCipher`): Handles in-place network packet encryption and decryption.
* **SHA-1** (`Sha1Hasher`): Computes the Minecraft server ID hash for online-mode authentication.
* **RSA-1024** (`RSA1024Encryptor`): Encrypts the shared secret and verify token during the login handshake.
* **Auth Join Request** (`AuthenticationProvider`): Sends the HTTP POST request to Mojang's session server
  (`https://sessionserver.mojang.com/session/minecraft/join`).

```kotlin
val client = createMinecraftClient(
    host = "127.0.0.1",
    username = "Player",
    accessToken = accessToken
) {
    // 1. AES-128-CFB8 Cipher Implementation
    cipherFactory = { sharedKey -> JvmAesCipher(sharedKey) }

    // 2. SHA-1 Hasher Implementation
    sha1Hasher = Sha1Hasher { serverId, secretKey, publicKey -> 
        /* your SHA-1 implementation */ 
    }

    // 3. RSA-1024 Encryptor Implementation
    rsaEncryptor = RSA1024Encryptor { publicKey, data -> 
        /* your RSA encryption implementation */ 
    }

    // 4. Mojang Auth HTTP Join Provider
    authProvider = AuthenticationProvider { accessToken, uuid, serverIdHash ->
        /* send HTTP POST to session server using your preferred HTTP client (Ktor, OkHttp, etc.) */
    }
}