# 组合上下文API

```kotlin
public val CustomProtocolContext: ProtocolContextBuilder.() -> Unit = {
    rsaEncryptor = RSA1024Encryptor { key, data -> ... }
    sha1Hasher = Sha1Hasher { serverId, secretKey, publicKey -> ... }
    cipherFactory = { key -> ... }
    authProvider = AuthenticationProvider { url, accessToken, uuid, serverIdHash -> ... }
    socketEngine = MyCustomTCPSocketImpl()
}
```

> 如果使用`libmc-protocol-context`中的`DefaultProtocolContext`作为默认上下文实现,
> 请参阅[实现 TCPSocket](Impl-TCP-Socket-zh.md)

# 修改默认实现

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

> 使用`.withCustom`来修改已有的上下文API实现

