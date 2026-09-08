# Creating a Client

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

> In the example code above, a `MinecraftClient` is created. This client will connect to an offline server at
> `127.0.0.1:25565` using `MyBot` as the player name, and replaces the underlying TCP Socket
> implementation with a `ktor-network` based TCP Socket. (For details on how to create a SocketEngine, please refer
> to [Implementing TCP Socket](Impl-tcp-socket.md). For details on how to
> create a Context, please refer to [Required APIs](README.md#required-apis))
> MinecraftClient implements CoroutineScope, and calling `client.connect()` will execute the connection on a background
> thread. Blocking thread to prevent the application from exiting

## Connecting to an Online-Mode Server

```kotlin
private val accessToken = "eyJraWQiOiIw..."

public fun main() = runBlocking {
    val client = createMinecraftClient(
        // Other parameters
        username = "RTAkland",
        uuid = Uuid.parse("bb033844-e68e-4909-a636-1a5d1821ddc4"),
        accessToken = accessToken,
        // Other parameters
    )
}
```

> In the example code above, the client will connect to the server using `RTAkland` as the player name.

# Get an AccessToken

Open [minecraft.net](https://minecraft.net), log in, press `F12`, and run the following code in the Console:

```javascript
console.log(`; ${document.cookie}`.split('; bearer_token=').pop().split(';').shift())
```

> Note: AccessTokens are valid for 24 hours

# Listening for Packets

```kotlin
// Listen for specific received packets (must start with Clientbound)
client.onPacket<ClientboundLoginSuccessPacket> {
    println(it)
}

// Listen for all received packets
client.on { packet, direction ->
    println("$direction ->$packet")
}

// Listen for outgoing packets (must start with Serverbound)
cli.onSent<ServerboundPongConfigurationPacket> {
    println(it)
}
```

# Sending Packets

```kotlin
// Only packets starting with Serverbound can be sent, otherwise an UnsupportedOperationException will be thrown
client.networkChannel.sendPacket(
    ServerboundChatCommandPacket(command = "say Hello from libmc")
)
```