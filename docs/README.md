# libmc

A lightweight, modern Minecraft client protocol library designed for Kotlin Native & JVM. The core protocol library
module relies on the following dependencies:

- Standard Library (`kotlin-stdlib`)
- I/O (`kotlinx-io`) - Efficiently wraps each packet into a Buffer
- Coroutines (`kotlinx-coroutines`) - Enables high-performance asynchronous operations

## Required APIs

| Module Name | Required    | Notes                                                                                                                                                 |
|:------------|:------------|:------------------------------------------------------------------------------------------------------------------------------------------------------|
| TCP Socket  | Yes         | The `protocol` module does not have a built-in TCP Socket implementation. [Implement TCP Socket](Impl-tcp-socket.md)                                  |
| HTTP Client | Conditional | Required only when logging into an `online-mode` server to send join request to mojang's session server. [Implement HTTP Client](Impl-http-client.md) |

# Get started

# Protocol

> `libmc-protocol` is current under development. It only supports the latest Minecraft version
> (Current supported Minecraft version: `26.2`, Protocol Version Number: `776`)

## NBT & SNBT

[NBT & SNBT](nbt-snbt.md)