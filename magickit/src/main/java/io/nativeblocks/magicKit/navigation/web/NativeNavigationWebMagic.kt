package io.nativeblocks.magicKit.navigation.web

import io.nativeblocks.core.api.provider.magic.INativeMagic
import io.nativeblocks.core.api.provider.magic.MagicProps
import io.nativeblocks.core.navigator.NativeNavigator
import io.nativeblocks.core.util.getVariableValue

class NativeNavigationWebMagic(private val nativeNavigator: NativeNavigator) : INativeMagic {

    override fun handle(magicProps: MagicProps) {
        val destinationField = magicProps.nativeTrigger?.properties?.get("destinationUrl")
        var destination = destinationField?.value

        magicProps.variables?.forEach { variable ->
            destination = destination?.getVariableValue(variable.key, variable.value.value)
        }
        destination = destination?.replace("{index}", magicProps.listItemIndex.toString())

        val inAppField = magicProps.nativeTrigger?.properties?.get("destinationInApp")

        if (inAppField?.value.toBoolean()) {
            nativeNavigator.navigateToInternalWeb(destination.orEmpty())
        } else {
            nativeNavigator.navigateToExternalWeb(destination.orEmpty())
        }
    }
}