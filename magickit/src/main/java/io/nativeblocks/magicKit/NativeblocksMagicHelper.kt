package io.nativeblocks.magicKit

import android.content.Context
import io.nativeblocks.core.api.NativeblocksManager
import io.nativeblocks.magicKit.alert.NativeToastMagic
import io.nativeblocks.magicKit.api.NativeRestApiClient
import io.nativeblocks.magicKit.api.NativeRestApiMagic
import io.nativeblocks.magicKit.delay.NativeDelayMagic
import io.nativeblocks.magicKit.navigation.internal.NativeNavigationMagic
import io.nativeblocks.magicKit.navigation.web.NativeNavigationWebMagic
import io.nativeblocks.magicKit.variable.NativeChangeVariableMagic

object NativeblocksMagicHelper {

    private val restApiClient = NativeRestApiClient()

    fun provideMagics(androidContext: Context) {
        NativeblocksManager.getInstance()
            .provideMagic(
                magicType = "NATIVE_NAVIGATION",
                magic = NativeNavigationMagic(NativeblocksManager.getInstance().getNativeNavigator())
            )
            .provideMagic(
                magicType = "NATIVE_NAVIGATION_WEB",
                magic = NativeNavigationWebMagic(NativeblocksManager.getInstance().getNativeNavigator())
            )
            .provideMagic(
                magicType = "NATIVE_REST_API",
                magic = NativeRestApiMagic(restApiClient)
            )
            .provideMagic(
                magicType = "NATIVE_ALERT",
                magic = NativeToastMagic(androidContext)
            )
            .provideMagic(
                magicType = "NATIVE_DELAY",
                magic = NativeDelayMagic()
            )
            .provideMagic(
                magicType = "NATIVE_CHANGE_VARIABLE",
                magic = NativeChangeVariableMagic()
            )
    }

}