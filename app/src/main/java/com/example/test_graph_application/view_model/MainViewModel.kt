package com.example.test_graph_application.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_graph_application.api.DatasetRequest
import com.example.test_graph_application.repository.Repository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(private val repository: Repository): ViewModel() {

    private val _dataset = MutableStateFlow<List<DatasetRequest>?>(null)
    val dataset = _dataset.asStateFlow()

    fun getDataset() {
        viewModelScope.launch {
            repository.getDataset()
                .onStart { Timber.d("Loading started") }
                .onCompletion { Timber.d("Loading stopped") }
                .catch { Timber.e(it) }
                .collect {
                    if (it.isSuccessful) {
                        _dataset.emit(it.body())
                    }
                }
        }
    }
}