# libmc

A lightweight minecraft related library, such as rcon client, motd ping and more for kotlin multiplatform and java, no
dependencies in `jvm` target except `kotlin-stdlib`

native platforms required `ktor-network` and `kotlinx-io`

# Supported targets

- jvm 1.8
- mingwX64
- linuxArm64
- liunxX64
- iosArm64
- iosSimulatorArm64
- macosArm64

# Get started

[Use mcping module](https://repo.rtast.cn/packages/-/cn.rtast.libmc:mcping/)

[Use rconlib module](https://repo.rtast.cn/packages/-/cn.rtast.libmc:rconlib/)

# MC Ping

## Kotlin example

```kotlin
fun main() {
    val sm = SelectorManager(Dispatchers.IO)
    // context is not required, if not passed, default context will be used
    // ping java server
    val response: PingResponse =
        mcping(host = "org.mc-complex.com", port = 25565, type = ServerType.Java, context = LibMCContext(sm))
    println(response.content)
    println(response.latency)

    // ping bedrock
    val response = mcping("play.wildnetwork.net", 19132, ServerType.Bedrock)
    println(response.toBedrockResponse())
}
```

## Java example

```java
void main() {
    // ping java server
    String testJavaHost = "org.mc-complex.com";
    PingResponse javaResponse = McPing.mcping(testJavaHost, 25565);

    // ping bedrock server
    String testBedrockHost = "play.wildnetwork.net";
    PingResponse bedrockResponse = McPing.mcping(testBedrockHost, 19132, ServerType.Bedrock);
    System.out.println(bedrockResponse.toBedrockResponse());
}
```

> java consumers should not manually pass the context parameter

## Other resources

> An example of java ping json response can be found at [ping-response-example](example/java-ping-response.json)
> (Formatted),
> a raw response of bedrock ping response can be found
> at [bedrock-pinng-raw-response](example/bedrock-pinng-raw-response.txt)

# rcon client

```kotlin
fun main() {
    val host = "127.0.0.1"
    val port = 25575
    val client = rconClient(host, port)
    val authed = client.connect("123456")
    if (authed) println(client.command("list")) else throw IllegalArgumentException("incorrect password")
}
```

> also supports java

```java
void main() {
    String host = "127.0.0.1";
    int port = 25575;
    String password = "123456";
    RCONClient rconClient = Rconlib.rconClient(host, port);
    boolean authed = rconClient.connect(password);
    if (authed) {
        System.out.println(rconClient.command("list"));
    } else {
        throw new IllegalStateException("incorrect password");
    }
}
```

# Open Source

Open source under [Apache-2.0](LICENSE)