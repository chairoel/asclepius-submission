package com.dicoding.asclepius.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.repository.HealthRepository
import com.dicoding.asclepius.ui.model.Prediction
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HealthRepository) : ViewModel() {

    fun getHistories() = repository.getHistories()
    suspend fun getHistoryForResult(id: String): Prediction? {
        val history = repository.getHistoryById(id)
        return history?.let {
            Prediction(
                id = it.id,
                label = it.predictionResult,
                score = it.confidenceScore,
                source = ""
            )
        }
    }

    fun saveHistory(user: HistoryEntity) {
        viewModelScope.launch {
            repository.insertHistory(user)
        }
    }

    fun deleteHistory(id: String) {
        viewModelScope.launch {
            repository.deleteHistoryById(id)
        }
    }
}