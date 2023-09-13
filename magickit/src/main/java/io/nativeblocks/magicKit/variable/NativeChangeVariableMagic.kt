package io.nativeblocks.magicKit.variable

import io.nativeblocks.core.api.provider.magic.INativeMagic
import io.nativeblocks.core.api.provider.magic.MagicProps
import io.nativeblocks.core.frame.domain.model.NativeVariableModel
import io.nativeblocks.core.util.getVariableValue
import io.nativeblocks.core.util.hasCondition
import io.nativeblocks.core.util.hasOperator
import io.nativeblocks.core.util.operatorCondition
import io.nativeblocks.core.util.operatorEvaluation

class NativeChangeVariableMagic : INativeMagic {

    override fun handle(magicProps: MagicProps) {
        val properties = magicProps.nativeTrigger?.properties.orEmpty()

        val variableKey = properties["variableKey"]?.value.orEmpty()
        val variableType = properties["variableType"]?.value ?: "STRING"
        val variableValue = properties["variableValue"]?.value.orEmpty()

        var value = variableValue
        magicProps.variables?.forEach { variable ->
            value = value.getVariableValue(variable.key, variable.value.value)
        }
        value = value.replace("{index}", magicProps.listItemIndex.toString())

        if (value.hasOperator()) {
            value = when (variableType) {
                "INT" -> value.operatorEvaluation()?.toInt().toString()
                "DOUBLE" -> value.operatorEvaluation()?.toDouble().toString()
                "LONG" -> value.operatorEvaluation()?.toLong().toString()
                "FLOAT" -> value.operatorEvaluation().toString()
                else -> value
            }
        }
        if (value.hasCondition()) {
            value = when (variableType) {
                "BOOLEAN" -> value.operatorCondition().toString()
                else -> value
            }
        }

        magicProps.onVariableChange?.invoke(
            NativeVariableModel(variableKey, value, variableType)
        )

        magicProps.nativeTrigger?.let { trigger ->
            magicProps.onHandleNextTrigger?.invoke(trigger)
        }
    }
}