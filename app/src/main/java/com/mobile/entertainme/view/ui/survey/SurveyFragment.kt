package com.mobile.entertainme.view.ui.survey

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.mobile.entertainme.databinding.FragmentSurveyBinding
import com.mobile.entertainme.view.MainActivity

class SurveyFragment : Fragment() {

    private var _binding: FragmentSurveyBinding? = null

    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var surveyViewModel: SurveyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        surveyViewModel =
            ViewModelProvider(this)[SurveyViewModel::class.java]

        _binding = FragmentSurveyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()

        binding.stressSurveySubmitBtn.setOnClickListener {
            submitSurvey()
        }

        surveyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        surveyViewModel.isSuccessful.observe(viewLifecycleOwner) { isSuccessful ->
            if (isSuccessful) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Survey submission failed", Toast.LENGTH_SHORT).show()
            }
        }

        surveyViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }

        binding.edStressFirstQuestion.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            val input = dest.toString() + source.toString()
            try {
                if (input.toInt() <= 15) {
                    null
                } else {
                    ""
                }
            } catch (e: NumberFormatException) {
                ""
            }
        })

        return root
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

            val firstQuestionAnswer = binding.edStressFirstQuestion.text.toString()
            if (firstQuestionAnswer.isEmpty()) {
                Toast.makeText(requireContext(), "Please answer all the questions first", Toast.LENGTH_SHORT).show()
                return
            } else {
                surveyData["stress_first_question"] = firstQuestionAnswer
            }

            // Checking if other questions are answered
            val allQuestionsAnswered = checkAllQuestionsAnswered()
            if (!allQuestionsAnswered) {
                Toast.makeText(requireContext(), "Please answer all the questions first", Toast.LENGTH_SHORT).show()
                return
            }

            addToSurveyDataIfChecked(binding.stressSecondQuestionOption1, "stress_second_question", surveyData)
            addToSurveyDataIfChecked(binding.stressSecondQuestionOption2, "stress_second_question", surveyData)
            addToSurveyDataIfChecked(binding.stressSecondQuestionOption3, "stress_second_question", surveyData)

            addToSurveyDataIfChecked(binding.stressThirdQuestionOption1, "stress_third_question", surveyData)
            addToSurveyDataIfChecked(binding.stressThirdQuestionOption2, "stress_third_question", surveyData)
            addToSurveyDataIfChecked(binding.stressThirdQuestionOption3, "stress_third_question", surveyData)

            addToSurveyDataIfChecked(binding.stressFourthQuestionOption1, "stress_fourth_question", surveyData)
            addToSurveyDataIfChecked(binding.stressFourthQuestionOption2, "stress_fourth_question", surveyData)
            addToSurveyDataIfChecked(binding.stressFourthQuestionOption3, "stress_fourth_question", surveyData)
            addToSurveyDataIfChecked(binding.stressFourthQuestionOption3, "stress_fourth_question", surveyData)

            addToSurveyDataIfChecked(binding.stressFifthQuestionOption1, "stress_fifth_question", surveyData)
            addToSurveyDataIfChecked(binding.stressFifthQuestionOption2, "stress_fifth_question", surveyData)
            addToSurveyDataIfChecked(binding.stressFifthQuestionOption3, "stress_fifth_question", surveyData)

            addToSurveyDataIfChecked(binding.stressSixthQuestionOption1, "stress_sixth_question", surveyData)
            addToSurveyDataIfChecked(binding.stressSixthQuestionOption2, "stress_sixth_question", surveyData)
            addToSurveyDataIfChecked(binding.stressSixthQuestionOption3, "stress_sixth_question", surveyData)

            addToSurveyDataIfChecked(binding.stressSeventhQuestionOption1, "stress_seventh_question", surveyData)
            addToSurveyDataIfChecked(binding.stressSeventhQuestionOption2, "stress_seventh_question", surveyData)
            addToSurveyDataIfChecked(binding.stressSeventhQuestionOption3, "stress_seventh_question", surveyData)

            addToSurveyDataIfChecked(binding.stressEighthQuestionOption1, "stress_eighth_question", surveyData)
            addToSurveyDataIfChecked(binding.stressEighthQuestionOption2, "stress_eighth_question", surveyData)

            surveyViewModel.submitSurvey(surveyData)
            surveyViewModel.fetchStressPrediction()
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAllQuestionsAnswered(): Boolean {
        return binding.stressSecondQuestionRadioGroup.checkedRadioButtonId != -1 &&
                binding.stressThirdQuestionRadioGroup.checkedRadioButtonId != -1 &&
                binding.stressFourthQuestionRadioGroup.checkedRadioButtonId != -1 &&
                binding.stressFifthQuestionRadioGroup.checkedRadioButtonId != -1 &&
                binding.stressSixthQuestionRadioGroup.checkedRadioButtonId != -1 &&
                binding.stressSeventhQuestionRadioGroup.checkedRadioButtonId != -1 &&
                binding.stressEighthQuestionRadioGroup.checkedRadioButtonId != -1
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}