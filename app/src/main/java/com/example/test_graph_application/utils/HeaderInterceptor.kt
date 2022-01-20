package com.example.test_graph_application.utils

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val hasAcceptHeader = chain.request().header("Accept").isNullOrEmpty()
        val hasContentTypeHeader = chain.request().header("Content-Type").isNullOrEmpty()

        val request = chain.request().newBuilder().run {

            if (!hasAcceptHeader) {
                header("Accept", "application/json")
            }

            if (!hasContentTypeHeader) {
                header("Content-Type", "application/json")
            }

            build()
        }

        return chain.proceed(request)
    }
}