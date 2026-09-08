# Assemble context API

```kotlin
public val CustomProtocolContext: ProtocolContextBuilder.() -> Unit = {
    rsaEncryptor = RSA1024Encryptor { key, data -> ... }
    sha1Hasher = Sha1Hasher { serverId, secretKey, publicKey -> ... }
    cipherFactory = { key -> ... }
    authProvider = AuthenticationProvider { url, accessToken, uuid, serverIdHash -> ... }
    socketEngine = MyCustomTCPSocketImpl()
}
```

> If using `DefaultProtocolContext` from `libmc-protocol-context` as the default context implementation,
> please refer to [Implementing TCPSocket](Impl-TCP-Socket.md)

# Modify exists context API implementation

```kotlin
fun main() {
    val myNewProtocolContext = DefaultProtocolContext.withCustom {
        rsaEncryptor = RSA1024Encryptor { key, data -> ... }
        sha1Hasher = Sha1Hasher { serverId, secretKey, publicKey -> ... }
        cipherFactory = { key -> ... }
        authProvider = AuthenticationProvider { url, accessToken, uuid, serverIdHash -> ... }
        socketEngine = MyCustomTCPSocketImpl()
    }
}
```

> Call `.withCustom` on a `ProtocolContextBuilder` to modify exists context implementation

