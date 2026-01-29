package com.dicoding.asclepius.ui.adapter.news

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.dto.ArticleDto
import com.dicoding.asclepius.databinding.ItemLayoutArticleBinding
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class NewsAdapter : ListAdapter<ArticleDto, NewsAdapter.ViewHolder>(NewsDiffCallback()) {

    class ViewHolder(val binding: ItemLayoutArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutArticleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)

        with(holder.binding) {
            tvTitle.text = article.title
            tvDateTime.text = formatNewsDate(article.publishedAt)
            tvDesc.text = article.description

            Glide.with(itemView.context)
                .load(article.urlToImage)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .fallback(R.drawable.ic_place_holder)
                )
                .into(ivImage)

            tvViewMore.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                itemView.context.startActivity(intent)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun formatNewsDate(date: String?): String {
        if (date.isNullOrBlank()) return "-"

        return try {
            val instant = Instant.parse(date)

            val formatter = DateTimeFormatter.ofPattern(
                "dd MMM yyyy • HH:mm",
                Locale("id", "ID")
            )

            instant
                .atZone(ZoneId.systemDefault())
                .format(formatter)

        } catch (e: Exception) {
            "-"
        }
    }
}