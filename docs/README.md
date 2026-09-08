# libmc

[中文](zh/README-zh.md)

A lightweight, modern Minecraft client protocol library designed for Kotlin Native & JVM. The core protocol library
module relies on the following dependencies:

- Standard Library (`kotlin-stdlib`)
- I/O (`kotlinx-io`) - Efficiently wraps each packet into a Buffer
- Coroutines (`kotlinx-coroutines`) - Enables high-performance asynchronous operations

## Required APIs

| Module Name  | Required    | Notes                                                                                                                                                           |
|:-------------|:------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| TCP Socket   | Yes         | The `protocol` module does not have a built-in TCP Socket implementation. [Implement TCP Socket](Impl-TCP-Socket.md)                                            |
| AES-128-CFB8 | Conditional | Required only when logging into an `online-mode` server to encrypt/decrypt traffic. [Implement AES-128-CFB8](Impl-Crypto.md#AES-128-CFB8)                       |
| RSA 1024     | Conditional | Required only when logging into an `online-mode` server to encrypt the shared secret with the server's public key. [Implement RSA 1024](Impl-Crypto.md#RSA1024) |
| SHA1         | Conditional | Required only when logging into an `online-mode` server to compute the Server ID hash. [Implement SHA1](Impl-Crypto.md#SHA1)                                    |
| HTTP Client  | Conditional | Required only when logging into an `online-mode` server to send join request to mojang's session server. [Implement HTTP Client](Impl-HTTP-Client.md)           |

# Get started

# Protocol

> `libmc-protocol` is current under development. It only supports the latest Minecraft version
> (Current supported Minecraft version: `26.2`, Protocol Version: `776`)

[Start using libmc-protocol](en/Get-started.md)

# Assemble all context APIs

[Assemble context APIs](en/Assemble-context.md)

## NBT & SNBT

[NBT & SNBT](en/NBT-SNBT.md)