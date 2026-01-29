package com.dicoding.asclepius.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.dao.HistoryDao
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.remote.api.ApiService

class HealthRepository private constructor(
    private val apiService: ApiService,
    private val historyDao: HistoryDao
) {

    suspend fun getNews() = apiService.getNews(
        query = "cancer",
        category = "health",
        language = "en"
    )

    fun getHistories(): LiveData<List<HistoryEntity>> {
        return historyDao.getHistory()
    }

    suspend fun getHistoryById(id: String): HistoryEntity? =
        historyDao.getHistoryById(id)

    suspend fun insertHistory(user: HistoryEntity) {
        historyDao.insertHistory(user)
    }

    suspend fun deleteHistoryById(id: String) {
        historyDao.deleteById(id)
    }

    companion object {
        @Volatile
        private var instance: HealthRepository? = null
        fun getInstance(
            apiService: ApiService,
            historyDao: HistoryDao
        ): HealthRepository =
            instance ?: synchronized(this) {
                instance ?: HealthRepository(apiService, historyDao)
            }.also { instance = it }
    }
}