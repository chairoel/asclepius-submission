package com.dicoding.asclepius.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.dao.HistoryDao
import com.dicoding.asclepius.data.local.entity.HistoryEntity

class HealthRepository private constructor(
    private val historyDao: HistoryDao
) {

    fun getHistories(): LiveData<List<HistoryEntity>> {
        return historyDao.getHistory()
    }

    fun getHistoryById(id: Long): LiveData<HistoryEntity> {
        return historyDao.getHistoryById(id)
    }

    suspend fun insertHistory(user: HistoryEntity) {
        historyDao.insertHistory(user)
    }

    suspend fun deleteFHistoryById(id: Long) {
        historyDao.deleteById(id)
    }

    companion object {
        @Volatile
        private var instance: HealthRepository? = null
        fun getInstance(
            historyDao: HistoryDao
        ): HealthRepository =
            instance ?: synchronized(this) {
                instance ?: HealthRepository(historyDao)
            }.also { instance = it }
    }
}