UNAME_S := $(shell uname -s)
UNAME_M := $(shell uname -m)

ifeq ($(UNAME_S), Darwin)
    ifeq ($(UNAME_M), arm64)
        ARCH ?= macos_arm64
    else
        ARCH ?= macos_x64
    endif
else
    ifeq ($(UNAME_M), aarch64)
        ARCH ?= arm64
    else
        ARCH ?= x86_64
    endif
endif

OUT_DIR = ./libs

ifeq ($(ARCH), arm64)
    CROSS_COMPILE ?= aarch64-linux-gnu-
    TARGET_DIR    = $(OUT_DIR)/linuxArm64
else ifeq ($(ARCH), win64)
    CROSS_COMPILE ?= x86_64-w64-mingw32-
    TARGET_DIR    = $(OUT_DIR)/mingwX64
else ifeq ($(ARCH), x86_64)
    CROSS_COMPILE ?=
    TARGET_DIR    = $(OUT_DIR)/linuxX64
else ifeq ($(ARCH), macos_arm64)
    CROSS_COMPILE ?= aarch64-apple-darwin20.4-
    TARGET_DIR    = $(OUT_DIR)/macosArm64
else ifeq ($(ARCH), macos_x64)
    CROSS_COMPILE ?= x86_64-apple-darwin20.4-
    TARGET_DIR    = $(OUT_DIR)/macosX64
else
    CROSS_COMPILE ?=
    TARGET_DIR    = $(OUT_DIR)/$(ARCH)
endif

CC           = $(CROSS_COMPILE)gcc
AR           = $(CROSS_COMPILE)ar

ARFLAGS      = rcs
CFLAGS       = -Wall -Os -c

ifdef AES192
CFLAGS += -DAES192=1
endif
ifdef AES256
CFLAGS += -DAES256=1
endif

# include path to AVR library
INCLUDE_PATH = /usr/lib/avr/include
SPLINT       = splint aes.c -I$(INCLUDE_PATH) +charindex -unrecog

.INTERMEDIATE: aes.o

default: lib

.SILENT:
.PHONY: lint clean lib all

all:
	@echo "Building for linuxX64..."
	$(MAKE) clean && $(MAKE) ARCH=x86_64
	@echo "Building for linuxArm64..."
	$(MAKE) clean && $(MAKE) ARCH=arm64
	@echo "Building for mingwX64..."
	$(MAKE) clean && $(MAKE) ARCH=win64
	@echo "Building for macosArm64..."
	$(MAKE) clean && $(MAKE) ARCH=macos_arm64
	@echo "Building for macosX64..."
	$(MAKE) clean && $(MAKE) ARCH=macos_x64
	$(MAKE) clean
	@echo "All platform binaries in $(OUT_DIR)/"

aes.o : aes.c aes.h
	echo [CC] $@ $(CFLAGS)
	$(CC) $(CFLAGS) -o $@ $<

$(TARGET_DIR)/libaes.a : aes.o
	mkdir -p $(TARGET_DIR)
	echo [AR] $@
	$(AR) $(ARFLAGS) $@ $^

lib : $(TARGET_DIR)/libaes.a

clean:
	rm -f *.OBJ *.LST *.o *.gch *.out *.a

lint:
	$(call SPLINT)