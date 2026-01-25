package com.dicoding.asclepius.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.repository.HealthRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HealthRepository) : ViewModel() {

    fun getHistories() = repository.getHistories()
    fun getHistory(id: Long) = repository.getHistoryById(id)

    fun saveHistory(user: HistoryEntity) {
        viewModelScope.launch {
            repository.insertHistory(user)
        }
    }

    fun deleteHistory(id: Long) {
        viewModelScope.launch {
            repository.deleteFHistoryById(id)
        }
    }
}