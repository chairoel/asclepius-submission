package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.local.database.AsclepiusDatabase
import com.dicoding.asclepius.data.remote.api.ApiConfig
import com.dicoding.asclepius.data.repository.HealthRepository

object Injection {
    fun provideRepository(context: Context): HealthRepository {
        val apiService = ApiConfig.getApiService()
        val database = AsclepiusDatabase.getInstance(context)
        val userDetailsDao = database.historyDto()
        return HealthRepository.getInstance(apiService, userDetailsDao)
    }
}