package com.dicoding.asclepius.ui.adapter.news

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.asclepius.data.remote.dto.ArticleDto
import com.dicoding.asclepius.ui.model.Prediction

class NewsDiffCallback : DiffUtil.ItemCallback<ArticleDto>() {
    override fun areItemsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
        return oldItem.urlToImage == newItem.urlToImage &&
                oldItem.title == newItem.title &&
                oldItem.publishedAt == newItem.publishedAt &&
                oldItem.description == newItem.description &&
                oldItem.url == newItem.url
    }
}