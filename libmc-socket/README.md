# libmc-socket

A general purpose kotlin multiplatform `TCP Socket` client library, no external dependencies needed, only
`java.net.Socket` and `posix/WinSock`.

It supports resolve `host`, `ipv4` and `ipv6`. Also provided a `DataTransformer` interface,
it means you can inject a transformer to modify the bytes before send and after receive.
This used for encrypt/decrypt data from/to an online-mode Minecraft server.

```kotlin
fun main() {
    val socket = NativeSocket("127.0.0.1", 25565)
    socket.connect()
    val readChannel = socket.openReadChannel()
    val writeChannel = socket.openWriteChannel()

    while (true) {
//        val data: Byte = readChannel.readByte()
        val data: ByteArray = readChannel.readBytes(1024)
        if (data.decodeToString() == "close") break else {
            writeChannel.send("Hello from client".encodeToByteArray())
        }
    }
    socket.close()
}
```