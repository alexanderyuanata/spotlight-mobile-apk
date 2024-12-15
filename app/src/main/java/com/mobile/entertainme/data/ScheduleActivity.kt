package com.mobile.entertainme.data

data class ScheduleActivity(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val date: String = "",
    val time: String = "",
    val completed: Boolean = false,
    val uid: String = "",
    var firebaseKey: String = ""
)
