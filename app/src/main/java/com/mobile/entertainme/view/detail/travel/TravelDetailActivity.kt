package com.mobile.entertainme.view.detail.travel

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
import com.mobile.entertainme.adapter.TravelDetailAdapter
import com.mobile.entertainme.databinding.ActivityTravelDetailBinding

class TravelDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTravelDetailBinding
    private val travelDetailViewModel: TravelDetailViewModel by viewModels()
    private lateinit var travelDetailAdapter: TravelDetailAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTravelDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        travelDetailAdapter = TravelDetailAdapter()

        binding.travelDetailList.apply {
            layoutManager = LinearLayoutManager(this@TravelDetailActivity)
            adapter = travelDetailAdapter
        }

        observeTravelDetailViewModel()

        val user = auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            travelDetailViewModel.fetchDetailTravel(uid)
        }
    }

    private fun observeTravelDetailViewModel() {
        travelDetailViewModel.travel.observe(this) { travel ->
            travel?.let {
                travelDetailAdapter.submitList(it)
            }
        }

        travelDetailViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}