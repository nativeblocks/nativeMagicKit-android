package io.nativeblocks.nativemagickit.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.nativeblocks.blockKit.NativeblocksBlockHelper
import io.nativeblocks.core.api.NativeblocksManager
import io.nativeblocks.core.api.NativeblocksProvider
import io.nativeblocks.magicKit.NativeblocksMagicHelper

private const val NATIVEBLOCKS_API_KEY =
    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3Y2FiY2FjMS00MzI3LTRkMWQtOTVmMS1hOGMyYzc4MjE3MzciLCJvcmciOiI1MmZiNjIzOC1kY2FiLTQwYTYtODdhZC02ZGYyYzliYzVlZDgiLCJpYXQiOjE2OTIzODQ0MTIsImV4cCI6MTcyMzkyMDQxMn0.0hF-KSXtcqxMNcar1OOBs-H9gDTkAoC903TXVw6bGDg"

private const val NATIVEBLOCKS_API_URL = "http://192.168.1.39:8585/graphql"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NativeblocksManager.initialize(this, NATIVEBLOCKS_API_URL, NATIVEBLOCKS_API_KEY)

        NativeblocksBlockHelper.provideBlocks()
        NativeblocksMagicHelper.provideMagics(this)

        setContent {
            NativeblocksProvider()
        }
    }
}
