CC ?= gcc
AR ?= ar
CFLAGS ?= -O2 -Wall -fPIC
LDFLAGS ?= -lresolv

OUT_DIR_X64 = libs/linuxX64
OUT_DIR_ARM64 = libs/linuxArm64
OUT_DIR_MACOS_ARM64 = libs/macosArm64

.PHONY: all test libs arm64 macos_arm64 clean

all: test libs arm64 macos_arm64

test: srv.c srv.h test_srv.c
	$(CC) $(CFLAGS) srv.c test_srv.c -o test_srv $(LDFLAGS)
	./test_srv hypixel.net

libs: $(OUT_DIR_X64)/libsrv.a

$(OUT_DIR_X64)/libsrv.a: srv.c srv.h
	mkdir -p $(OUT_DIR_X64)
	$(CC) $(CFLAGS) -c srv.c -o $(OUT_DIR_X64)/srv.o
	$(AR) rcs $@ $(OUT_DIR_X64)/srv.o
	rm -f $(OUT_DIR_X64)/srv.o
	@echo "Linux X86-64: $@"

arm64:
	mkdir -p $(OUT_DIR_ARM64)
	aarch64-linux-gnu-gcc $(CFLAGS) -c srv.c -o $(OUT_DIR_ARM64)/srv.o
	aarch64-linux-gnu-ar rcs $(OUT_DIR_ARM64)/libsrv.a $(OUT_DIR_ARM64)/srv.o
	rm -f $(OUT_DIR_ARM64)/srv.o
	@echo "Linux ARM64: $(OUT_DIR_ARM64)/libsrv.a"

macos_arm64:
	mkdir -p $(OUT_DIR_MACOS_ARM64)
	clang $(CFLAGS) -arch arm64 -c srv.c -o $(OUT_DIR_MACOS_ARM64)/srv.o
	ar rcs $(OUT_DIR_MACOS_ARM64)/libsrv.a $(OUT_DIR_MACOS_ARM64)/srv.o
	rm -f $(OUT_DIR_MACOS_ARM64)/srv.o
	@echo "macOS ARM64: $(OUT_DIR_MACOS_ARM64)/libsrv.a"

clean:
	rm -rf test_srv libs