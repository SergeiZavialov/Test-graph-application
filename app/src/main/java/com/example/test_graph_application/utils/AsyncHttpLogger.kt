package com.example.test_graph_application.utils

import com.example.test_graph_application.BuildConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class AsyncHttpLogger : HttpLoggingInterceptor.Logger {

    companion object {
        private const val MAX_LOG_LENGTH = 3300
        private const val LOG_BUFFER = 1000
    }

    private val logsQueue = MutableSharedFlow<String>(LOG_BUFFER)

    var enableLogs: Boolean = BuildConfig.DEBUG

    init {
        GlobalScope.launch {
            logsQueue.collect { msg ->
                Timber.tag("OkHttp").d(msg)
            }
        }
    }

    override fun log(message: String) {
        if (!enableLogs) return
        var tempMessage = message

        for (i in 0..(tempMessage.length / MAX_LOG_LENGTH)) {
            val lastIndex = if (tempMessage.length < MAX_LOG_LENGTH) {
                tempMessage.length
            } else {
                MAX_LOG_LENGTH
            }
            logsQueue.tryEmit(tempMessage.substring(0 until lastIndex))
            tempMessage = tempMessage.removeRange(0 until lastIndex)
        }
    }

}