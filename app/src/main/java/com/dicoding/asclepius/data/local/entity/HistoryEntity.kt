package com.dicoding.asclepius.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
class HistoryEntity(
    @field:PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @field:ColumnInfo(name = "image_uri")
    val imageUri: String,

    @field:ColumnInfo(name = "prediction")
    val predictionResult: String,

    @field:ColumnInfo(name = "score")
    val confidenceScore: Float,

    @field:ColumnInfo(name = "created_at")
    val createdAt: Long,
)