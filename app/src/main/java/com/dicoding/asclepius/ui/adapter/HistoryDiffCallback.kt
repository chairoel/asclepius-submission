package com.dicoding.asclepius.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.asclepius.ui.model.Prediction

class HistoryDiffCallback(
    private val oldList: List<Prediction>,
    private val newList: List<Prediction>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].source == newList[newItemPosition].source
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].source != newList[newItemPosition].source -> {
                false
            }

            oldList[oldItemPosition].label != newList[newItemPosition].label -> {
                false
            }

            oldList[oldItemPosition].score != newList[newItemPosition].score -> {
                false
            }

            else -> true
        }
    }
}