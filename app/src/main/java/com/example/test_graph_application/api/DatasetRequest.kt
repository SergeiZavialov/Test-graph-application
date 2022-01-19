package com.example.test_graph_application.api

import com.google.gson.annotations.SerializedName

class DatasetRequest(
    @SerializedName("_time")
    val time: Double,
    @SerializedName("CPU")
    val cpu: Float,
    @SerializedName("MEM")
    val mem: Float
)