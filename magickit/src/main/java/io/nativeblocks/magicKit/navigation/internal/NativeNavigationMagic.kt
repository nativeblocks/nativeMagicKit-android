package io.nativeblocks.magicKit.navigation.internal

import io.nativeblocks.core.api.provider.magic.INativeMagic
import io.nativeblocks.core.api.provider.magic.MagicProps
import io.nativeblocks.core.navigator.NAVIGATION_BACK
import io.nativeblocks.core.navigator.NativeNavigator
import io.nativeblocks.core.util.getVariableValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NativeNavigationMagic(private val nativeNavigator: NativeNavigator) : INativeMagic {

    override fun handle(magicProps: MagicProps) {
        val destinationField = magicProps.nativeTrigger?.properties?.get("destinationUrl")
        var destination = destinationField?.value

        magicProps.variables?.forEach { variable ->
            destination = destination?.getVariableValue(variable.key, variable.value.value)
        }
        destination = destination?.replace("{index}", magicProps.listItemIndex.toString())

        magicProps.coroutineScope.launch(Dispatchers.Main) {
            if (destination == NAVIGATION_BACK) {
                nativeNavigator.popBackStack()
            } else {
                nativeNavigator.navigateTo(destination.orEmpty())
            }
        }
    }
}