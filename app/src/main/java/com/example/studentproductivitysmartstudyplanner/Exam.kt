package com.example.studentproductivitysmartstudyplanner

data class Exam(
    var id: Int = 0,
    var subject: String,
    var title: String,
    var date: String,
    var time: String,
    var location: String
)