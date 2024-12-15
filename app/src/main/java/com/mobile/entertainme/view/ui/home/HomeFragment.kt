package com.mobile.entertainme.view.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.mobile.entertainme.R
import com.mobile.entertainme.adapter.BookAdapter
import com.mobile.entertainme.adapter.MovieAdapter
import com.mobile.entertainme.adapter.TravelAdapter
import com.mobile.entertainme.api.ApiConfig
import com.mobile.entertainme.databinding.FragmentHomeBinding
import com.mobile.entertainme.response.BookResponse
import com.mobile.entertainme.response.MovieResponse
import com.mobile.entertainme.view.detail.book.BookDetailActivity
import com.mobile.entertainme.view.detail.movie.MovieDetailActivity
import com.mobile.entertainme.view.detail.travel.TravelDetailActivity
import com.mobile.entertainme.view.login.LoginActivity
import com.mobile.entertainme.view.ui.survey.SurveyFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var bookAdapter: BookAdapter
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var travelAdapter: TravelAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()

        bookAdapter = BookAdapter()
        movieAdapter = MovieAdapter()
        travelAdapter = TravelAdapter()

        homeViewModel.username.observe(viewLifecycleOwner) { username ->
            binding.username.text = username
        }

        homeViewModel.books.observe(viewLifecycleOwner) { books ->
            showLoading(binding.bookProgressBar, false)
            bookAdapter.submitList(books)
        }

        homeViewModel.movies.observe(viewLifecycleOwner) { movies ->
            showLoading(binding.movieProgressBar, false)
            movieAdapter.submitList(movies)
        }

        homeViewModel.travel.observe(viewLifecycleOwner) { travel ->
            showLoading(binding.travelProgressBar, false)
            travelAdapter.submitList(travel)
        }

        homeViewModel.stressPrediction.observe(viewLifecycleOwner) { prediction ->
            val stressLevel = when (prediction) {
                null -> getString(R.string.none)
                else -> prediction.toString()
            }
            binding.stressLabel.text = stressLevel

            // Update margin based on prediction value
            val stressTextLayoutParams = binding.stressText.layoutParams as ConstraintLayout.LayoutParams
            val marginStartDp = if (prediction == null) 80 else 100
            stressTextLayoutParams.marginStart = requireContext().dpToPx(marginStartDp)
            binding.stressText.layoutParams = stressTextLayoutParams

            // Update color based on prediction value
            val color = when (prediction) {
                in 1..4 -> R.color.light_blue
                in 5..7 -> R.color.yellow
                in 8..10 -> R.color.red
                else -> R.color.light_blue
            }
            binding.stressContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
        }

        binding.logout.setOnClickListener {
            homeViewModel.clearGoogleSignInInfo()
            homeViewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        binding.seeAllBooks.setOnClickListener {
            startActivity(Intent(requireContext(), BookDetailActivity::class.java))
        }

        binding.seeAllMovies.setOnClickListener {
            startActivity(Intent(requireContext(), MovieDetailActivity::class.java))
        }

        binding.seeAllTravels.setOnClickListener {
            startActivity(Intent(requireContext(), TravelDetailActivity::class.java))
        }

        setupRecyclerView()
        fetchBooks()
        fetchMovies()
        fetchTravel()

        return root
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.fetchStressLevel()
    }

    private fun setupRecyclerView() {
        binding.bookRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bookAdapter
        }

        binding.movieRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = movieAdapter
        }

        binding.travelRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = travelAdapter
        }
    }

    private fun fetchBooks() {
        showLoading(binding.bookProgressBar, true)
        homeViewModel.fetchBooks()
    }

    private fun fetchMovies() {
        showLoading(binding.movieProgressBar, true)
        homeViewModel.fetchMovies()
    }

    private fun fetchTravel() {
        showLoading(binding.travelProgressBar, true)
        homeViewModel.fetchTravel()
    }

    private fun showLoading(progressBar: ProgressBar, isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun Context.dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}