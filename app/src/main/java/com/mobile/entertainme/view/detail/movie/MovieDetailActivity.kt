package com.mobile.entertainme.view.detail.movie

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.mobile.entertainme.R
import com.mobile.entertainme.adapter.MovieDetailAdapter
import com.mobile.entertainme.databinding.ActivityMovieDetailBinding

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
    private lateinit var movieDetailAdapter: MovieDetailAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        movieDetailAdapter = MovieDetailAdapter()

        binding.movieDetailList.apply {
            layoutManager = LinearLayoutManager(this@MovieDetailActivity)
            adapter = movieDetailAdapter
        }

        observeMovieDetailViewModel()

        val user = auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            movieDetailViewModel.fetchDetailMovies(uid)
        }
    }

    private fun observeMovieDetailViewModel() {
        movieDetailViewModel.movies.observe(this) { movies ->
            movieDetailAdapter.submitList(movies)
        }

        movieDetailViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}