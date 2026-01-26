package com.dicoding.asclepius.ui.model

import java.util.Locale

data class Prediction(
    val id: String,
    val source: String,
    val label: String,
    val score: Float,
) {
    override fun toString(): String =
        "$label (${String.format(Locale.getDefault(), "%.1f%%", score * 100)})"
}
