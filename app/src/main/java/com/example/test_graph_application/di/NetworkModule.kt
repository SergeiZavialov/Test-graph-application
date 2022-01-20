package com.example.test_graph_application.di

import com.example.test_graph_application.api.ApiService
import com.example.test_graph_application.utils.HeaderInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideApi(get()) }
    single { provideGson() }
}

fun provideOkHttpClient() = OkHttpClient.Builder()
    .readTimeout(1, TimeUnit.MINUTES)
    .connectTimeout(10, TimeUnit.SECONDS)
    .addInterceptor(HeaderInterceptor())
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl("http://80.211.168.161")
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()

fun provideGson() = Gson()

fun provideApi(retrofit: Retrofit) = retrofit.create(ApiService::class.java)