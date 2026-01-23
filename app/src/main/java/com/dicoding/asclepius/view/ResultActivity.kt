package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        imageUriString?.let { uriStr ->
            val imageUri = Uri.parse(uriStr)
            binding.resultImage.setImageURI(imageUri)

            val helper = ImageClassifierHelper(this)
            helper.classifyStaticImage(imageUri)
            binding.resultText.text = helper.predictionResult ?: "Analisis gagal"
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}