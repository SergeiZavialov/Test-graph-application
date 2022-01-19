package com.example.test_graph_application

import android.app.Application
import com.example.test_graph_application.di.networkModule
import com.example.test_graph_application.di.repositoryModule
import com.example.test_graph_application.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(
                networkModule,
                repositoryModule,
                viewModelsModule
            )
        }
    }
}