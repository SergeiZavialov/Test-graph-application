package com.example.test_graph_application.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming

interface ApiService {
    @GET("/dataset_cpu_mem.json")
    @Streaming
    suspend fun downloadFile(): ResponseBody
}