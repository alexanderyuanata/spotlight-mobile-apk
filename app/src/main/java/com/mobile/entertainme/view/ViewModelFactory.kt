package com.mobile.entertainme.view

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobile.entertainme.view.add.AddScheduleViewModel
import com.mobile.entertainme.view.detail.book.BookDetailViewModel
import com.mobile.entertainme.view.detail.movie.MovieDetailViewModel
import com.mobile.entertainme.view.detail.travel.TravelDetailViewModel
import com.mobile.entertainme.view.login.LoginViewModel
import com.mobile.entertainme.view.recommendsurvey.RecommendationSurveyViewModel
import com.mobile.entertainme.view.signup.SignupViewModel
import com.mobile.entertainme.view.ui.home.HomeViewModel
import com.mobile.entertainme.view.ui.schedule.ScheduleViewModel
import com.mobile.entertainme.view.ui.survey.SurveyViewModel

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(application) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(application) as T
            modelClass.isAssignableFrom(RecommendationSurveyViewModel::class.java) -> RecommendationSurveyViewModel(application) as T
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> SignupViewModel(application) as T
            modelClass.isAssignableFrom(ScheduleViewModel::class.java) -> ScheduleViewModel(application) as T
            modelClass.isAssignableFrom(SurveyViewModel::class.java) -> SurveyViewModel(application) as T
            modelClass.isAssignableFrom(BookDetailViewModel::class.java) -> BookDetailViewModel(application) as T
            modelClass.isAssignableFrom(MovieDetailViewModel::class.java) -> MovieDetailViewModel(application) as T
            modelClass.isAssignableFrom(TravelDetailViewModel::class.java) -> TravelDetailViewModel(application) as T
            modelClass.isAssignableFrom(AddScheduleViewModel::class.java) -> AddScheduleViewModel(application) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}