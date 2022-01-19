package com.example.test_graph_application.repository

import com.example.test_graph_application.api.ApiService
import com.example.test_graph_application.api.DatasetRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class Repository(private val api: ApiService) {

    fun getDataset(): Flow<Response<List<DatasetRequest>>> = flow {
        emit(api.getDataset())
    }.flowOn(Dispatchers.IO)
}