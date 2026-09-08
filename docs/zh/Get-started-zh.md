# 创建客户端

```kotlin
public fun main() = runBlocking {
    val client = createMinecraftClient(
        "127.0.0.1", 25566, "MyBot",
        generateOfflineUuid("MyBot"),
        accessToken = null,
        context = DefaultProtocolContext.withCustom {
            socketEngine = KtorNetworkEngine()
        }
    )
    client.launch { client.connect() }
    while (true) {
        delay(5.seconds)
    }
}
```

> 在上面的示例代码中, 创建了一个`MinecraftClient`, 这个客户端将会使用`MyBot`作为玩家名称连接到`127.0.0.1:25565`的`离线`
> 服务器, 并且将底层TCP Socket实现替换为了基于`ktor-network`实现的TCP Socket.
> (有关如何创建`SocketEngine`请参阅[实现TCP Socket](Impl-TCP-Socket-zh.md),
> 有关如何创建Context请参阅[需要实现的API](README-zh.md#需要实现的api))
> `MinecraftClient`实现了`CoroutineContext`, 使用`client.connect()`后将会切换到后台线程执行, 添加阻塞线程代码以免程序直接退出.

## 连接到在线服务器

```kotlin
private val accessToken = "eyJraWQiOiIw..."

public fun main() = runBlocking {
    val client = createMinecraftClient(
        // 其他参数
        username = "RTAkland",
        uuid = Uuid.parse("bb033844-e68e-4909-a636-1a5d1821ddc4"),
        accessToken = accessToken,
        // 其他参数
    )
}
```

> 上面的示例代码中, 将会使用`RTAkland`作为玩家名称连接到服务器

### 快速获取AccessToken

打开[minecraft.net](https://minecraft.net)并登录后按下F12, 在Console内输入

```javascript
console.log(`; ${document.cookie}`.split('; bearer_token=').pop().split(';').shift())
```

> AccessToken的有效期为24小时

# 监听数据包

```kotlin
// 监听接收到的指定类型的数据包, 必须以Clientbound开头
client.onPacket<ClientboundLoginSuccessPacket> {
    println(it)
}
// 监听所有接收到的数据包
client.on { packet, direction ->
    println("$direction -> $packet")
}

// 监听被发送出去的数据包, 必须以Serverbound开头
cli.onSent<ServerboundPongConfigurationPacket> {
    println(it)
}
```

# 发送数据包

```kotlin
// 必须以Serverbound开头的数据包才可以被发送, 否则将会抛出UnsupportedOperationException异常
client.networkChannel.sendPacket(
    ServerboundChatCommandPacket(command = "say Hello from libmc")
)
```