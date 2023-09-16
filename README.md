# Nativeblocks android magicKit

## Get started

Root gradle

```bash
repositories {
    google()
    mavenCentral()
    maven {
        url  "https://jitpack.io"
    }
}
```

Module gradle

```bash
dependencies {
    implementation ("com.github.nativeblocks:nativeMagicKit-android:0.1.2")
}
```

In this step, the developer needs to provide the magics to the Nativeblocks SDK. To accomplish this, the following code should be integrated:

```js
NativeblocksMagicHelper.provideMagics(this)
```


Here is version compatibility of magicKit and Nativeblocks core SDK

| magic kit version | Nativeblocks core version |
|-------------------|---------------------------|
| 0.1.2             | 0.1.4                     |
| 0.1.1             | 0.1.3                     |
