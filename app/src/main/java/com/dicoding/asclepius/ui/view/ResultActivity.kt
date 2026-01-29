package com.dicoding.asclepius.ui.view

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.ViewModelFactory
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.ui.adapter.news.NewsAdapter
import com.dicoding.asclepius.ui.model.Prediction
import com.dicoding.asclepius.ui.viewmodel.HistoryViewModel
import com.dicoding.asclepius.ui.viewmodel.ResultViewModel
import kotlinx.coroutines.launch
import com.dicoding.asclepius.data.Result
import com.google.android.material.R as MR

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val viewModel: ResultViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var predictionResult: Prediction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Hasil Analisa"

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val historyId = intent.getStringExtra(EXTRA_ID)
        val source = intent.getStringExtra(EXTRA_SOURCE)

        imageUriString?.let { uriStr ->
            val imageUri = Uri.parse(uriStr)
            binding.resultImage.setImageURI(imageUri)

            when {
                source == MainActivity::class.java.simpleName -> {
                    val helper = ImageClassifierHelper(this)
                    helper.classifyStaticImage(imageUri)
                    predictionResult = helper.predictionResult
                    binding.resultText.text = predictionResult?.toString() ?: "Analisis gagal"
                    setPredictionTextColor(predictionResult?.label)
                }

                historyId != null -> {
                    lifecycleScope.launch {
                        predictionResult = historyViewModel.getHistoryForResult(historyId)
                        predictionResult?.let {
                            binding.resultText.text = predictionResult?.toString()
                            setPredictionTextColor(predictionResult?.label)
                        } ?: run {
                            Toast.makeText(
                                this@ResultActivity,
                                "Riwayat tidak ditemukan",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                }

                else -> {}
            }
        }

        val newsAdapter = NewsAdapter()
        binding.rvArticle.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ResultActivity)
            adapter = newsAdapter
        }

        viewModel.listArticle.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.loading.visibility = View.VISIBLE
                    binding.rvArticle.visibility = View.GONE
                    binding.tvArticleLabel.visibility = View.GONE
                    binding.tvArticleLabelDesc.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.loading.visibility = View.GONE
                    binding.rvArticle.visibility = View.VISIBLE
                    binding.tvArticleLabel.visibility = View.VISIBLE
                    binding.tvArticleLabelDesc.visibility = View.VISIBLE
                    newsAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.loading.visibility = View.GONE
                    binding.rvArticle.visibility = View.GONE
                    binding.tvArticleLabel.visibility = View.GONE
                    binding.tvArticleLabelDesc.visibility = View.GONE
                    Toast.makeText(this@ResultActivity, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSave.apply {
            if (source == MainActivity::class.java.simpleName) {
                setIconResource(R.drawable.ic_save_24)
                text = getString(R.string.save)
            } else {
                setIconResource(R.drawable.ic_delete_24)
                text = getString(R.string.delete)

                val typedValue =
                    context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorError))
                val errorColor = typedValue.getColor(0, 0)
                typedValue.recycle()

                val typedValue2 =
                    context.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorOnError))
                val onErrorColor = typedValue2.getColor(0, 0)
                typedValue2.recycle()

                backgroundTintList = ColorStateList.valueOf(errorColor)
                setTextColor(onErrorColor)
                iconTint = ColorStateList.valueOf(onErrorColor)
            }
        }

        binding.btnSave.setOnClickListener {
            predictionResult?.let { result ->
                val history = HistoryEntity(
                    id = result.id,
                    imageUri = result.source,
                    predictionResult = result.label,
                    confidenceScore = result.score,
                    createdAt = System.currentTimeMillis()
                )

                if (source == MainActivity::class.java.simpleName) {
                    historyViewModel.saveHistory(history)
                    Toast.makeText(this, "Riwayat disimpan!", Toast.LENGTH_SHORT).show()
                } else {
                    historyViewModel.deleteHistory(result.id)
                    Toast.makeText(this, "Riwayat dihapus!", Toast.LENGTH_SHORT).show()
                }
                finish()
            } ?: Toast.makeText(this, "Data tidak tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPredictionTextColor(label: String?) {
        val isNonCancer = label?.contains("Non", ignoreCase = true) == true

        val attr = if (isNonCancer) {
            MR.attr.colorPrimary
        } else {
            MR.attr.colorError
        }

        val typedArray = theme.obtainStyledAttributes(intArrayOf(attr))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()

        binding.resultText.setTextColor(color)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_SOURCE = "extra_source_intent"
    }
}