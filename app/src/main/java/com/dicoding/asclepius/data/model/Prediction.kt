package com.dicoding.asclepius.data.model

import java.util.Locale

data class Prediction(
    val source: String,
    val label: String,
    val score: Float,
) {
    override fun toString(): String =
        "$label (${String.format(Locale.getDefault(), "%.1f%%", score * 100)})"
}
