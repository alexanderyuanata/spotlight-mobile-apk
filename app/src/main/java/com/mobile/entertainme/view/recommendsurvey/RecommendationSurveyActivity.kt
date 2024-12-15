package com.mobile.entertainme.view.recommendsurvey

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.mobile.entertainme.R
import com.mobile.entertainme.databinding.ActivityRecommendationSurveyBinding
import com.mobile.entertainme.view.MainActivity
import com.mobile.entertainme.view.ViewModelFactory

class RecommendationSurveyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationSurveyBinding
    private lateinit var recommendationSurveyViewModel: RecommendationSurveyViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecommendationSurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        recommendationSurveyViewModel = ViewModelProvider(this, ViewModelFactory(application))[RecommendationSurveyViewModel::class.java]

        setupRadioGroupListeners()

        binding.recommendationSurveySubmitBtn.setOnClickListener {
            submitSurvey()
        }

        recommendationSurveyViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        recommendationSurveyViewModel.isSuccessful.observe(this) { isSuccessful ->
            if (isSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Survey submission failed", Toast.LENGTH_SHORT).show()
            }
        }

        recommendationSurveyViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }

        binding.edMovieFourthQuestion.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            val input = dest.toString() + source.toString()
            try {
                if (input.toInt() <= 10) {
                    null
                } else {
                    ""
                }
            } catch (e: NumberFormatException) {
                ""
            }
        })

        binding.edMovieThirdQuestion.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.contains(" ") || source.contains(",")) {
                ""
            } else {
                null
            }
        })

    }

    private fun setupRadioGroupListeners() {
        val radioGroups = listOf(
            binding.bookFirstQuestionRadioGroup to Pair(binding.bookFirstQuestionOption2.id, binding.edBookFirstQuestion),
            binding.bookSecondQuestionRadioGroup to Pair(binding.bookSecondQuestionOption2.id, binding.edBookSecondQuestion),
            binding.bookThirdQuestionRadioGroup to Pair(binding.bookThirdQuestionOption2.id, binding.edBookThirdQuestion),
            binding.movieFifthQuestionRadioGroup to Pair(binding.movieFifthQuestionOption2.id, binding.edMovieFifthQuestion),
            binding.movieSixthQuestionRadioGroup to Pair(binding.movieSixthQuestionOption2.id, binding.edMovieSixthQuestion),
            binding.tourFirstQuestionRadioGroup to Pair(binding.tourFirstQuestionOption2.id, binding.edTourFirstQuestion),
            binding.tourSecondQuestionRadioGroup to Pair(binding.tourSecondQuestionOption2.id, binding.edTourSecondQuestion)
        )

        for ((radioGroup, pair) in radioGroups) {
            val (_, editText) = pair
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val isOption2Checked = checkedId == pair.first

                editText.isEnabled = isOption2Checked
                if (!isOption2Checked) {
                    editText.text = null
                }
            }

            if (radioGroup.checkedRadioButtonId == -1) {
                editText.isEnabled = false
                editText.text = null
            }
        }
    }

    private fun addToSurveyDataIfChecked(view: View, questionKey: String, surveyData: HashMap<String, Any>) {
        if (view is RadioButton && view.isChecked) {
            surveyData[questionKey] = view.text.toString()
        } else if (view is EditText) {
            val text = view.text.toString().trim()
            if (text.isNotEmpty()) {
                surveyData[questionKey] = text
            }
        }
    }

    private fun submitSurvey() {
        val surveyData = HashMap<String, Any>()
        val uid = auth.currentUser?.uid

        if (uid != null) {
            surveyData["user_id"] = uid

            if (!allQuestionsAnswered()) {
                Toast.makeText(this, "Please answer all the questions first", Toast.LENGTH_SHORT).show()
                return
            }

            addToSurveyDataIfChecked(binding.bookFirstQuestionOption1, "book_first_question", surveyData)
            addToSurveyDataIfChecked(binding.edBookFirstQuestion, "book_first_question", surveyData)

            addToSurveyDataIfChecked(binding.bookSecondQuestionOption1, "book_second_question", surveyData)
            addToSurveyDataIfChecked(binding.edBookSecondQuestion, "book_second_question", surveyData)

            addToSurveyDataIfChecked(binding.bookThirdQuestionOption1, "book_third_question", surveyData)
            addToSurveyDataIfChecked(binding.edBookThirdQuestion, "book_third_question", surveyData)

            addToSurveyDataIfChecked(binding.bookFourthQuestionOption1, "book_fourth_question", surveyData)
            addToSurveyDataIfChecked(binding.bookFourthQuestionOption2, "book_fourth_question", surveyData)
            addToSurveyDataIfChecked(binding.bookFourthQuestionOption3, "book_fourth_question", surveyData)
            addToSurveyDataIfChecked(binding.bookFourthQuestionOption4, "book_fourth_question", surveyData)

            addToSurveyDataIfChecked(binding.bookFifthQuestionOption1, "book_fifth_question", surveyData)
            addToSurveyDataIfChecked(binding.bookFifthQuestionOption2, "book_fifth_question", surveyData)
            addToSurveyDataIfChecked(binding.bookFifthQuestionOption3, "book_fifth_question", surveyData)
            addToSurveyDataIfChecked(binding.bookFifthQuestionOption4, "book_fifth_question", surveyData)

            addToSurveyDataIfChecked(binding.movieFirstQuestionOption1, "movie_first_question", surveyData)
            addToSurveyDataIfChecked(binding.movieFirstQuestionOption2, "movie_first_question", surveyData)
            addToSurveyDataIfChecked(binding.movieFirstQuestionOption3, "movie_first_question", surveyData)
            addToSurveyDataIfChecked(binding.movieFirstQuestionOption4, "movie_first_question", surveyData)

            addToSurveyDataIfChecked(binding.movieSecondQuestionOption1, "movie_second_question", surveyData)
            addToSurveyDataIfChecked(binding.movieSecondQuestionOption2, "movie_second_question", surveyData)
            addToSurveyDataIfChecked(binding.movieSecondQuestionOption3, "movie_second_question", surveyData)

            surveyData["movie_third_question"] = binding.edMovieThirdQuestion.text.toString()

            surveyData["movie_fourth_question"] = binding.edMovieFourthQuestion.text.toString()

            addToSurveyDataIfChecked(binding.movieFifthQuestionOption1, "movie_fifth_question", surveyData)
            addToSurveyDataIfChecked(binding.edMovieFifthQuestion, "movie_fifth_question", surveyData)

            addToSurveyDataIfChecked(binding.movieSixthQuestionOption1, "movie_sixth_question", surveyData)
            addToSurveyDataIfChecked(binding.edMovieSixthQuestion, "movie_sixth_question", surveyData)

            addToSurveyDataIfChecked(binding.movieSeventhQuestionOption1, "movie_seventh_question", surveyData)
            addToSurveyDataIfChecked(binding.movieSeventhQuestionOption2, "movie_seventh_question", surveyData)
            addToSurveyDataIfChecked(binding.movieSeventhQuestionOption3, "movie_seventh_question", surveyData)

            addToSurveyDataIfChecked(binding.tourFirstQuestionOption1, "tour_first_question", surveyData)
            addToSurveyDataIfChecked(binding.edTourFirstQuestion, "tour_first_question", surveyData)

            addToSurveyDataIfChecked(binding.tourSecondQuestionOption1, "tour_second_question", surveyData)
            addToSurveyDataIfChecked(binding.edTourSecondQuestion, "tour_second_question", surveyData)

            addToSurveyDataIfChecked(binding.tourThirdQuestionOption1, "tour_third_question", surveyData)
            addToSurveyDataIfChecked(binding.tourThirdQuestionOption2, "tour_third_question", surveyData)
            addToSurveyDataIfChecked(binding.tourThirdQuestionOption3, "tour_third_question", surveyData)
            addToSurveyDataIfChecked(binding.tourThirdQuestionOption4, "tour_third_question", surveyData)
            addToSurveyDataIfChecked(binding.tourThirdQuestionOption5, "tour_third_question", surveyData)
            addToSurveyDataIfChecked(binding.tourThirdQuestionOption6, "tour_third_question", surveyData)

            addToSurveyDataIfChecked(binding.tourFourthQuestionOption1, "tour_fourth_question", surveyData)
            addToSurveyDataIfChecked(binding.tourFourthQuestionOption2, "tour_fourth_question", surveyData)
            addToSurveyDataIfChecked(binding.tourFourthQuestionOption3, "tour_fourth_question", surveyData)
            addToSurveyDataIfChecked(binding.tourFourthQuestionOption4, "tour_fourth_question", surveyData)
            addToSurveyDataIfChecked(binding.tourFourthQuestionOption5, "tour_fourth_question", surveyData)

            addToSurveyDataIfChecked(binding.tourFifthQuestionOption1, "tour_fifth_question", surveyData)
            addToSurveyDataIfChecked(binding.tourFifthQuestionOption2, "tour_fifth_question", surveyData)
            addToSurveyDataIfChecked(binding.tourFifthQuestionOption3, "tour_fifth_question", surveyData)
            addToSurveyDataIfChecked(binding.tourFifthQuestionOption4, "tour_fifth_question", surveyData)

            addToSurveyDataIfChecked(binding.tourSixthQuestionOption1, "tour_sixth_question", surveyData)
            addToSurveyDataIfChecked(binding.tourSixthQuestionOption2, "tour_sixth_question", surveyData)
            addToSurveyDataIfChecked(binding.tourSixthQuestionOption3, "tour_sixth_question", surveyData)

            recommendationSurveyViewModel.submitSurvey(surveyData)
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allQuestionsAnswered(): Boolean {
        val editTextQuestions = listOf(
            binding.edBookFirstQuestion,
            binding.edBookSecondQuestion,
            binding.edBookThirdQuestion,
            binding.edMovieFifthQuestion,
            binding.edMovieSixthQuestion,
            binding.edTourFirstQuestion,
            binding.edTourSecondQuestion
        )

        val radioGroupQuestions = listOf(
            binding.bookFirstQuestionRadioGroup,
            binding.bookSecondQuestionRadioGroup,
            binding.bookThirdQuestionRadioGroup,
            binding.movieFifthQuestionRadioGroup,
            binding.movieSixthQuestionRadioGroup,
            binding.tourFirstQuestionRadioGroup,
            binding.tourSecondQuestionRadioGroup
        )

        for (editText in editTextQuestions) {
            if (editText.isEnabled && editText.text.toString().trim().isEmpty()) {
                return false
            }
        }

        for (radioGroup in radioGroupQuestions) {
            if (radioGroup.checkedRadioButtonId == -1) {
                return false
            }
        }

        return true
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}