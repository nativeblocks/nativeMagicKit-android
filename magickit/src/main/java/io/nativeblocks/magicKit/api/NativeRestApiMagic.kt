package io.nativeblocks.magicKit.api

import io.nativeblocks.core.api.provider.magic.INativeMagic
import io.nativeblocks.core.api.provider.magic.MagicProps
import io.nativeblocks.core.util.getVariableValue
import kotlinx.coroutines.launch

class NativeRestApiMagic(
    private val nativeRestApiClient: NativeRestApiClient
) : INativeMagic {
    override fun handle(magicProps: MagicProps) {

        val properties = magicProps.nativeTrigger?.properties.orEmpty()

        var endpointUrl = properties["endpointUrl"]?.value.orEmpty()
        val method = properties["method"]?.value.orEmpty()
        val headers = properties["headers"]?.value.orEmpty()
        val body = properties["body"]?.value.orEmpty()

        val successResponseCodeKey = properties["successResponseCodeKey"]?.value.orEmpty()
        val successResponseBodyKey = properties["successResponseBodyKey"]?.value.orEmpty()
        val successResponseHeaderKey = properties["successResponseHeaderKey"]?.value.orEmpty()

        val successResponseCodeVariable = magicProps.variables?.get(successResponseCodeKey)
        val successResponseBodyVariable = magicProps.variables?.get(successResponseBodyKey)
        val successResponseHeaderVariable = magicProps.variables?.get(successResponseHeaderKey)

        val failureResponseCodeKey = properties["failureResponseCodeKey"]?.value.orEmpty()
        val failureResponseBodyKey = properties["failureResponseBodyKey"]?.value.orEmpty()
        val failureResponseHeaderKey = properties["failureResponseHeaderKey"]?.value.orEmpty()

        val failureResponseCodeVariable = magicProps.variables?.get(failureResponseCodeKey)
        val failureResponseBodyVariable = magicProps.variables?.get(failureResponseBodyKey)
        val failureResponseHeaderVariable = magicProps.variables?.get(failureResponseHeaderKey)

        magicProps.variables?.forEach { variable ->
            endpointUrl = endpointUrl.getVariableValue(variable.key, variable.value.value)
        }

        magicProps.coroutineScope.launch {
            nativeRestApiClient.performRequest(
                restApiRequestModel = RestApiRequestModel(
                    url = endpointUrl,
                    method = HttpApiRequestMethod.find(method),
                    headers = parseJsonObject(headers),
                    bodyForm = parseJsonObject(body),
                ),
                onSuccess = {
                    magicProps.onVariableChange?.invoke(
                        successResponseCodeVariable?.copy(value = it.httpCode.toString())
                    )
                    magicProps.onVariableChange?.invoke(
                        successResponseBodyVariable?.copy(value = it.httpBodyString.toString())
                    )
                    magicProps.onVariableChange?.invoke(
                        successResponseHeaderVariable?.copy(value = it.httpHeader.toString())
                    )
                    magicProps.nativeTrigger?.let { trigger ->
                        magicProps.onHandleSuccessNextTrigger?.invoke(trigger)
                    }
                },
                onFailure = {
                    if (it is HttpApiException) {
                        magicProps.onVariableChange?.invoke(
                            failureResponseCodeVariable?.copy(value = it.httpCode.toString())
                        )
                        magicProps.onVariableChange?.invoke(
                            failureResponseBodyVariable?.copy(value = it.httpBodyString.toString())
                        )
                        magicProps.onVariableChange?.invoke(
                            failureResponseHeaderVariable?.copy(value = it.httpHeader.toString())
                        )
                    }
                    magicProps.nativeTrigger?.let { trigger ->
                        magicProps.onHandleFailureNextTrigger?.invoke(trigger)
                    }
                }
            )
        }
    }
}