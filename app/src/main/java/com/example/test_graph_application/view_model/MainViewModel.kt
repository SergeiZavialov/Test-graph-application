package com.example.test_graph_application.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_graph_application.api.Dataset
import com.example.test_graph_application.repository.Repository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _dataset = MutableStateFlow<List<Dataset>?>(null)
    val dataset = _dataset.asStateFlow()

    fun getDataset() {
        viewModelScope.launch {
            repository.getFromOrLoadFileToCacheDir()
                .onStart { Timber.d("Start Loading") }
                .onCompletion { Timber.d("Stop Loading") }
                .catch { Timber.e(it) }
                .collect {
                    _dataset.emit(it)
                }
        }
    }
}