package com.example.test_graph_application.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.test_graph_application.R
import com.example.test_graph_application.api.Dataset
import com.example.test_graph_application.databinding.ActivityMainBinding
import com.example.test_graph_application.view_model.MainViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding<ActivityMainBinding>()
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.getDataset()
        setupChartView()
        setupObserver()
    }

    private fun setupChartView() {
        with(binding) {
            lineChart.apply {
                setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.white))

                xAxis.apply {
                    setDrawGridLinesBehindData(true)
                    position = XAxis.XAxisPosition.TOP

                }

                axisRight.apply {
                    setDrawZeroLine(true)
                }
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            viewModel.dataset.collect {
                it ?: return@collect
                setupChart(it)
            }
        }
    }

    private fun setupChart(list: List<Dataset>) {
        binding.lineChart.data = LineData(makeLineDataset(list))
    }

    private fun makeLineDataset(data: List<Dataset>): LineDataSet =
        LineDataSet(data.map { it.toEntry() }, "").apply {
            setDrawCircles(true)
        }
}