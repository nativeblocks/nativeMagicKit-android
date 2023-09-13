package io.nativeblocks.magicKit.delay

import io.nativeblocks.core.api.provider.magic.INativeMagic
import io.nativeblocks.core.api.provider.magic.MagicProps
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NativeDelayMagic : INativeMagic {

    override fun handle(magicProps: MagicProps) {
        val delay = magicProps.nativeTrigger?.properties?.get("delay")?.value.orEmpty()
        magicProps.coroutineScope.launch {
            delay(delay.toLongOrNull() ?: 0)
            magicProps.nativeTrigger?.let { trigger ->
                magicProps.onHandleNextTrigger?.invoke(trigger)
            }
        }
    }
}