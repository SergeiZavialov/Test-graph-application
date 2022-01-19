package com.example.test_graph_application.di

import com.example.test_graph_application.repository.Repository
import org.koin.dsl.module

val repositoryModule = module {
    single { Repository(get()) }
}