package io.nativeblocks.magicKit.restApi

import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.util.concurrent.TimeUnit

data class RestApiRequestModel(
    val url: String,
    val method: HttpApiRequestMethod,
    val headers: Map<String, Any>?,
    val bodyForm: Map<String, Any>?,
)

enum class HttpApiRequestMethod(private val method: String) {
    GET("GET"),
    POST("POST"),
    PATCH("PATCH"),
    PUT("PUT"),
    DELETE("DELETE");

    companion object {
        fun find(method: String): HttpApiRequestMethod {
            return values().firstOrNull { it.method == method } ?: return GET
        }

    }
}

class NativeRestApiClient {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .build()


    fun performRequest(
        restApiRequestModel: RestApiRequestModel,
        onSuccess: (HttpApiResponse) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val prepareRequest = Request.Builder().url(restApiRequestModel.url)

        val body = FormBody.Builder()
        restApiRequestModel.bodyForm?.forEach {
            body.add(it.key, it.value.toString())
        }
        when (restApiRequestModel.method) {
            HttpApiRequestMethod.GET -> prepareRequest.get()
            HttpApiRequestMethod.POST -> prepareRequest.post(body.build())
            HttpApiRequestMethod.PATCH -> prepareRequest.patch(body.build())
            HttpApiRequestMethod.PUT -> prepareRequest.put(body.build())
            HttpApiRequestMethod.DELETE -> prepareRequest.delete(body.build())
        }
        restApiRequestModel.headers?.forEach {
            prepareRequest.addHeader(it.key, it.value.toString())
        }

        val request = prepareRequest.build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure.invoke(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    try {
                        onSuccess.invoke(HttpApiResponse(response))
                    } catch (ex: Exception) {
                        onFailure.invoke(ex)
                    }
                } else {
                    onFailure.invoke(HttpApiException(response))
                }
            }
        })
    }
}

class HttpApiException(response: Response) : RuntimeException() {
    val httpCode = response.code
    val httpMessage: String = response.message
    val httpHeader: Headers = response.headers
    val httpBody: ResponseBody? = response.body
    val httpBodyString: String? = response.body?.string()
}

class HttpApiResponse(response: Response) {
    val httpCode = response.code
    val httpMessage: String = response.message
    val httpHeader: Headers = response.headers
    val httpBody: ResponseBody? = response.body
    val httpBodyString: String? = response.body?.string()
}