package com.dicoding.asclepius.ui.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.ui.viewmodel.MainViewModel
import com.yalantis.ucrop.UCrop
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val uCropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data!!)
                if (resultUri != null) {
                    viewModel.setCurrentImageUri(resultUri)
                }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val error = UCrop.getError(result.data!!)
                error?.printStackTrace()
                showToast("Gagal crop gambar")
            }
        }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startCrop(uri)
        } else {
            showToast("No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBar.toolbar)
        supportActionBar?.title = getText(R.string.app_name)

        binding.galleryButton.setOnClickListener { startGallery() }

        binding.analyzeButton.setOnClickListener {
            val imageUri = viewModel.currentImageUri.value
            if (imageUri != null) {
                analyzeImage(imageUri)
                moveToResult(imageUri)
            } else {
                showToast("Pilih gambar dulu!")
            }
        }

        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        viewModel.currentImageUri.observe(this) { uri ->
            uri?.let {
                binding.previewImageView.setImageURI(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_history -> {
                val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }


    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private fun startCrop(sourceUri: Uri) {
        val destinationUri =
            Uri.fromFile(File(cacheDir, "cropped_${System.currentTimeMillis()}.jpg"))

        val intent = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .getIntent(this)

        uCropLauncher.launch(intent)
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        // Karena saya pakai ViewModel untuk handle imageUri nya jadi function ini tidak saya pakai
        // Saya melakukan ini untuk meng-handle configuration change
    }

    private fun analyzeImage(imageUri: Uri) {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        ImageClassifierHelper(context = this@MainActivity).classifyStaticImage(imageUri)
    }

    private fun moveToResult(imageUri: Uri) {
        val intent = Intent(this@MainActivity, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_SOURCE, MainActivity::class.java.simpleName)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}