package com.dicoding.asclepius.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ItemLayoutHistoryBinding
import com.dicoding.asclepius.ui.model.Prediction
import java.util.Locale

class HistoryAdapter(
    private var onItemClickCallback: HistoryClickListener
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var oldList = emptyList<Prediction>()

    class ViewHolder(val binding: ItemLayoutHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLayoutHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(oldList[position]) {
                binding.tvAnalyzeLabel.text = label

                val scorePercent = String.format(Locale.getDefault(), "%.1f%%", score * 100)
                binding.tvAnalyzeScore.text =
                    itemView.context.getString(R.string.accuracy_score, scorePercent)

                Glide.with(itemView.context)
                    .load(source)
                    .apply(RequestOptions().fitCenter())
                    .into(binding.ciAnalyzeImage)
                binding.itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    fun getItem(index: Int): Prediction {
        return oldList[index]
    }

    fun setItems(newList: List<Prediction>) {
        val diffUtil = HistoryDiffCallback(oldList, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        diffResults.dispatchUpdatesTo(this)
    }
}