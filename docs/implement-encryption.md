* **AES-128-CFB8** (`NetworkCipher`): Handles in-place network packet encryption and decryption.
* **SHA-1** (`Sha1Hasher`): Computes the Minecraft server ID hash for online-mode authentication.
* **RSA-1024** (`RSA1024Encryptor`): Encrypts the shared secret and verify token during the login handshake.
* **Auth Join Request** (`AuthenticationProvider`): Sends the HTTP POST request to Mojang's session server
  (`https://sessionserver.mojang.com/session/minecraft/join`).

[Use libmc-protocol-encrypt](https://repo.rtast.cn/packages/-/cn.rtast.libmc:protocol-encrypt)

```kotlin
fun main() {
    val client = createMinecraftClient(
      // other parameter
      crypto = DefaultProtocolContext
    )
}
```