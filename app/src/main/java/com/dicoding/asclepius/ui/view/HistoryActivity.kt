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
import com.dicoding.asclepius.ui.adapter.HistoryAdapter
import com.dicoding.asclepius.ui.adapter.HistoryClickListener
import com.dicoding.asclepius.ui.model.Prediction
import com.dicoding.asclepius.ui.viewmodel.HistoryViewModel

class HistoryActivity : AppCompatActivity(), HistoryClickListener {

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

        historyAdapter = HistoryAdapter(this@HistoryActivity)
        binding.rvHistory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = historyAdapter
        }

        viewModel.getHistories().observe(this) { histories ->
            val data: ArrayList<Prediction> = ArrayList()
            histories.map { history ->
                val prediction = Prediction(
                    source = history.imageUri,
                    label = history.predictionResult,
                    score = history.confidenceScore
                )
                data.add(prediction)
            }
            historyAdapter.setItems(data)
            binding.tvEmptyData.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onItemClicked(position: Int) {
        val item = historyAdapter.getItem(position)
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, item.source)
        intent.putExtra(ResultActivity.EXTRA_SOURCE, HistoryActivity::class.java.simpleName)
        startActivity(intent)
    }
}