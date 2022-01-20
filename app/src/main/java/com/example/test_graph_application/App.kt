package com.example.test_graph_application

import android.app.Application
import android.os.StrictMode
import com.example.test_graph_application.di.networkModule
import com.example.test_graph_application.di.repositoryModule
import com.example.test_graph_application.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        plantTimberTree()
        initKoin()
    }

    private fun plantTimberTree() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            StrictMode.enableDefaults()
        }
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