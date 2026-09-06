# libmc-protocol-encrypt

This module implemented `ProtocolCryptoContext` and provided a `DefaultProtocolCryptoContext`.

## Get started

> `Sha1`, `RSA1024`, `AES-128-CFB8` from `cryptography-kotlin`(and its based provider).
> `HTTP Client` from `ktor-client`

> Before start, you need to add a `ktor client engine` for your platform. For JVM, `ktor-client-okhttp`(JVM 1.8+)
> or `ktor-client-java`(JVM 11+), for Linux, use `ktor-client-curl`, for Windows, use `ktor-client-winhttp`,
> for Apple, use `ktor-client-darwin`

```kotlin
fun main() {
    val cli = createMinecraftClient(
        // ... other paramater
        crypto = DefaultProtocolCryptoContext
    )
}
```