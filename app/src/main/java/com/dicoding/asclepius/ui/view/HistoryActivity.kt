package com.dicoding.asclepius.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.ViewModelFactory
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.ui.adapter.history.HistoryAdapter
import com.dicoding.asclepius.ui.model.Prediction
import com.dicoding.asclepius.ui.viewmodel.HistoryViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: HistoryViewModel by viewModels {
            ViewModelFactory.getInstance(this)
        }

        setSupportActionBar(binding.appBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.history)

        historyAdapter = HistoryAdapter { predictionId, imageUri ->
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri)
                putExtra(ResultActivity.EXTRA_ID, predictionId)
                putExtra(ResultActivity.EXTRA_SOURCE, HistoryActivity::class.java.simpleName)
            }
            startActivity(intent)
        }

        binding.rvHistory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = historyAdapter
        }

        viewModel.getHistories().observe(this) { histories ->
            val predictions = histories.map { history ->
                Prediction(
                    id = history.id,
                    source = history.imageUri,
                    label = history.predictionResult,
                    score = history.confidenceScore
                )
            }
            historyAdapter.submitList(predictions)
            binding.tvEmptyData.visibility = if (predictions.isEmpty()) View.VISIBLE else View.GONE
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}