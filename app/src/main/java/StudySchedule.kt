package com.example.studentproductivitysmartstudyplanner

data class StudySchedule(
    var id: Int = 0,
    var subject: String,
    var topic: String,
    var day: String,
    var startTime: String,
    var endTime: String
)