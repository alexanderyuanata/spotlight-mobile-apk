package com.mobile.entertainme.view.detail.book

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
import com.mobile.entertainme.adapter.BookDetailAdapter
import com.mobile.entertainme.databinding.ActivityBookDetailBinding

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding
    private lateinit var bookDetailAdapter: BookDetailAdapter
    private val bookDetailViewModel: BookDetailViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        bookDetailAdapter = BookDetailAdapter()

        binding.bookDetailList.layoutManager = LinearLayoutManager(this)
        binding.bookDetailList.adapter = bookDetailAdapter

        observeBookDetailViewModel()

        val user = auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            bookDetailViewModel.fetchDetailBooks(uid)
        } else {
            // Handle case where user is not logged in
        }
    }

    private fun observeBookDetailViewModel() {
        bookDetailViewModel.books.observe(this) { books ->
            bookDetailAdapter.submitList(books)
        }

        bookDetailViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}