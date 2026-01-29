package com.dicoding.asclepius.ui.adapter.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ItemLayoutHistoryBinding
import com.dicoding.asclepius.ui.model.Prediction
import java.util.Locale

class HistoryAdapter(
    private val onItemClick: (String, String) -> Unit
) : ListAdapter<Prediction, HistoryAdapter.ViewHolder>(HistoryDiffCallback()) {

    class ViewHolder(val binding: ItemLayoutHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prediction = getItem(position)

        with(holder.binding) {
            tvAnalyzeLabel.text = prediction.label

            val scorePercent = String.format(Locale.getDefault(), "%.1f%%", prediction.score * 100)
            tvAnalyzeScore.text = itemView.context.getString(R.string.accuracy_score, scorePercent)

            Glide.with(itemView.context)
                .load(prediction.source)
                .apply(RequestOptions().fitCenter())
                .into(ciAnalyzeImage)

            itemView.setOnClickListener {
                onItemClick(prediction.id, prediction.source)
            }
        }
    }
}