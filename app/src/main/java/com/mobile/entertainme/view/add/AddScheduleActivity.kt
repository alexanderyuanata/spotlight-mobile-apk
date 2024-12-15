package com.mobile.entertainme.view.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobile.entertainme.R
import com.mobile.entertainme.databinding.ActivityAddScheduleBinding
import java.util.Calendar

class AddScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddScheduleBinding
    private val viewModel: AddScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        val categories = resources.getStringArray(R.array.category_list)
        val categoryList = ArrayAdapter(this, R.layout.category_dropdown_item, categories)
        binding.categoryAutoComplete.setAdapter(categoryList)

        binding.dateInputEditText.setOnClickListener { showDatePicker() }
        binding.timeInputEditText.setOnClickListener { showTimePicker() }

        binding.addScheduleBtn.setOnClickListener {
            val title = binding.edActivityTitle.text.toString().trim()
            val description = binding.edActivityDescription.text.toString().trim()
            val category = binding.categoryAutoComplete.text.toString().trim()
            val date = binding.dateInputEditText.text.toString().trim()
            val time = binding.timeInputEditText.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty() && category.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
                viewModel.addSchedule(title, description, category, date, time)
            }
        }

        observeAddScheduleViewModel()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            binding.dateInputEditText.setText(selectedDate)
        }, year, month, day).show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, hourOfDay, minute ->
            val selectedTime = "$hourOfDay:$minute"
            binding.timeInputEditText.setText(selectedTime)
        }, hour, minute, true).show()
    }

    private fun observeAddScheduleViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                showDialog("Success", "Activity successfully added.") {
                    finish()
                }
            } else {
                showDialog("Failure", "Failed to add activity. Please try again.") {
                }
            }
        }
    }

    private fun showDialog(title: String, message: String, onPositiveButtonClick: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onPositiveButtonClick()
            }
            .show()
    }
}