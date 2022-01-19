package com.example.test_graph_application.utils

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val hasAcceptHeader = chain.request().header("Accept").isNullOrEmpty()
        val hasContentTypeHeader = chain.request().header("Content-Type").isNullOrEmpty()

        val request = chain.request().newBuilder().run {

            if (!hasAcceptHeader) {
                header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
            }

            if (!hasContentTypeHeader) {
                header("Content-Type", "application/json")
            }

            build()
        }

        return chain.proceed(request)
    }
}