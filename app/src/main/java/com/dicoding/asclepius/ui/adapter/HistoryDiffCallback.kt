package com.dicoding.asclepius.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.asclepius.ui.model.Prediction

class HistoryDiffCallback : DiffUtil.ItemCallback<Prediction>() {
    override fun areItemsTheSame(oldItem: Prediction, newItem: Prediction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Prediction, newItem: Prediction): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.label == newItem.label &&
                oldItem.score == newItem.score
    }
}