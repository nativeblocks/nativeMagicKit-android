package io.nativeblocks.magicKit.alert

import android.content.Context
import android.widget.Toast
import io.nativeblocks.core.api.provider.magic.INativeMagic
import io.nativeblocks.core.api.provider.magic.MagicProps
import io.nativeblocks.core.util.getVariableValue

class NativeToastMagic(
    private val androidContext: Context
) : INativeMagic {

    override fun handle(magicProps: MagicProps) {
        val message = magicProps.nativeTrigger?.properties?.get("message")

        Toast.makeText(
            androidContext,
            message?.value.orEmpty()
                .getVariableValue(message?.key.orEmpty(), message?.value.orEmpty()),
            Toast.LENGTH_LONG
        ).show()
    }
}