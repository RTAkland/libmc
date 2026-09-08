# libmc

一个轻量级适用于Kotlin Native & Jvm的现代Minecraft客户端协议库, 核心协议库模块使用了以下依赖

- 标准库 (`kotlin-stdlib`)
- IO (`kotlinx-io`) - 高效的将每个数据包封装为一个Buffer
- 协程库 (`kotlinx-coroutines`) - 实现高性能异步操作

## 需要实现的API

| 名称         | 是否必须实现 | 描述                                                                                                                                           |
|:-------------|:-------------|:-----------------------------------------------------------------------------------------------------------------------------------------------|
| TCP Socket   | 是           | `protocol` 模块没有内置TCP Socket实现. [实现 TCP Socket](Impl-TCP-Socket-zh.md)                                                                |
| AES-128-CFB8 | 视情况而定   | 仅在登录开启了正版验证(`online-mode`)的服务器时需要, 用于加密/解密流量. [实现AES-128-CFB8](Impl-Crypto-zh.md#AES-128-CFB8)                     |
| RSA 1024     | 视情况而定   | 仅在登录开启了正版验证(`online-mode`)的服务器时需要, 用于对服务器下发的公钥进行签名. [实现RSA 1024](Impl-Crypto-zh.md#RSA1024)                 |
| SHA1         | 视情况而定   | 仅在登录开启了正版验证(`online-mode`)的服务器时需要, 用于计算服务器的ServerId. [实现SHA1](Impl-Crypto-zh.md#SHA1)                              |
| HTTP 客户端  | 视情况而定   | 仅在登录开启了正版验证(`online-mode`)的服务器时需要, 用于向Mojang的Session服务器发送加入服务器的请求 [实现HTTP 客户端](Impl-HTTP-Client-zh.md) |

# 开始使用

> `libmc-protocol`目前正在开发中, 也许存在着某些未被发现的问题.
> 它仅支持最新的Minecraft版本 (目前支持的游戏版本: `26.2`, 协议版本: `776`)

[开始使用libmc-protocol](Get-started-zh.md)

# 将实现的API组合起来

[将所有上下文API组合](Assemble-context-zh.md)