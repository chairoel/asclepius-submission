package com.dicoding.asclepius.ui.view

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.ViewModelFactory
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.ui.model.Prediction
import com.dicoding.asclepius.ui.viewmodel.HistoryViewModel

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

        val source = intent.getStringExtra(EXTRA_SOURCE)

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
        const val EXTRA_SOURCE = "extra_source_intent"
    }
}