package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.data.ViewModelFactory
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.model.Prediction
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.viewmodel.HistoryViewModel

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private val viewModel: HistoryViewModel by viewModels {
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
        imageUriString?.let { uriStr ->
            val imageUri = Uri.parse(uriStr)
            binding.resultImage.setImageURI(imageUri)

            val helper = ImageClassifierHelper(this)
            helper.classifyStaticImage(imageUri)
            predictionResult = helper.predictionResult
            binding.resultText.text = predictionResult?.toString() ?: "Analisis gagal"
        }

        binding.btnSave.setOnClickListener {
            predictionResult?.let { result ->
                val history = HistoryEntity(
                    imageUri = result.source,
                    predictionResult = result.label,
                    confidenceScore = result.score,
                    createdAt = System.currentTimeMillis()
                )

                viewModel.saveHistory(history)
                Toast.makeText(this, "Riwayat disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}