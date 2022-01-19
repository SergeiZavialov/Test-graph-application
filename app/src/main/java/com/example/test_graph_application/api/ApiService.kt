package com.example.test_graph_application.api

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("dataset_cpu_mem.json")
    suspend fun getDataset(): Response<List<DatasetRequest>>
}