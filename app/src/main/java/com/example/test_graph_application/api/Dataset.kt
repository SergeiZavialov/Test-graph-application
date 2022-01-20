package com.example.test_graph_application.api

import com.github.mikephil.charting.data.Entry
import com.google.gson.annotations.SerializedName

class Dataset(
    @SerializedName("_time")
    val time: Double,
    @SerializedName("CPU")
    val cpu: Double,
    @SerializedName("MEM")
    val mem: Double
) {
    fun toEntry(): Entry = Entry(time.toFloat(), cpu.toFloat())
}