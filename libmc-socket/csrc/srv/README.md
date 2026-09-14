A c wrapper for libresolv on linuxX64/linuxArm64

Install compiler 

```shell
# for linux
sudo apt update
sudo apt install -y build-essential 
sudo apt install -y gcc-aarch64-linux-gnu binutils-aarch64-linux-gnu  # Cross-compilation arm64
```

Build

```shell
make all # x64 & arm64
make libs # x64
make arm64 # arm64
make test # test srv

make macos_arm64 # macos arm64
```

Static libraries generated at `libs`