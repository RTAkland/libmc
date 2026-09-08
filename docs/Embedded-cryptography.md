Starting with version `0.2.0` (`26.2-0.2.0`), `libmc` includes a built-in cryptography implementation.
It uses the JDK's built-in cryptographic APIs on the JVM platform and a pure-Kotlin implementation 
on `kotlin-native` platforms. However, due to a lack of low-level optimizations, performance results
in encoding and decoding tests are significantly slower than JDK's built-in implementations

A simplified table below shows the benchmark results:

| Algorithm                | Data Size (Bytes) | libmc's Built-in (ops/s) | JDK's Built-in (ops/s) | Gap                                        |
|:-------------------------|:------------------|:-------------------------|:-----------------------|:-------------------------------------------|
| **AES-128-CFB8 Encrypt** | 32                | 112,554.27               | 1,527,235.11           | **~13.6 times slower than JDK's built-in** |
|                          | 1,024             | 2,976.95                 | 49,842.94              | **~16.7 times slower than JDK's built-in** |
|                          | 65,536            | 45.04                    | 773.46                 | **~17.2 times slower than JDK's built-in** |
| **AES-128-CFB8 Decrypt** | 32                | 115,031.69               | 1,515,218.15           | **~13.2 times slower than JDK's built-in** |
|                          | 1,024             | 2,925.74                 | 49,868.55              | **~17.0 times slower than JDK's built-in** |
|                          | 65,536            | 48.47                    | 777.11                 | **~16.0 times slower than JDK's built-in** |
| **RSA-1024 Encrypt**     | 16                | 163.87                   | 91,600.51              | **~559 times slower than JDK's built-in**  |
|                          | 64                | 158.27                   | 93,042.81              | **~587 times slower than JDK's built-in**  |
|                          | 117               | 160.95                   | 94,826.94              | **~589 times slower than JDK's built-in**  |
| **SHA-1 Hashing**        | 64                | 2,134,974.72             | 11,609,497.92          | **~5.4 times slower than JDK's built-in**  |
|                          | 1,024             | 259,804.80               | 1,856,575.14           | **~7.1 times slower than JDK's built-in**  |
|                          | 65,536            | 4,485.95                 | 32,132.67              | **~7.2 times slower than JDK's built-in**  |

> **Benchmark Environment:**
> - **CPU:** AMD Ryzen 5 5500U (6 Cores / 12 Threads @ 2.10GHz)
> - **RAM:** 16GB DDR4 2667MHz
> - **OS:** Windows 11 64-bit
> - **Runtime:** Microsoft Build of OpenJDK 17.0.8, Kotlin 2.4.10 (MingwX64 & JVM)

**Fortunately**, except for `AES-128-CFB8` (which requires continuous stream encryption/decryption during networking),
the other operations (RSA & SHA-1) are only executed once during the initial server authentication phase

If you have optimized native algorithm implementations (via `cinterop` or other approaches), PRs are welcome