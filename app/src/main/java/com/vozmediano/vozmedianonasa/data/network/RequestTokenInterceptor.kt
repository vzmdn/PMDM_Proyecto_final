package com.vozmediano.vozmedianonasa.data.network

import okhttp3.Interceptor
import okhttp3.Response

class RequestTokenInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("api_key", token)
            .build()

        val requestWithToken = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(requestWithToken)
    }
}