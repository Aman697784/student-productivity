package com.example.studentproductivitysmartstudyplanner

data class Task(

    var id: Int = 0,

    var title: String,

    var description: String,

    var subject: String,

    var category: String,

    var dueDate: String,

    var priority: String,

    var status: String
)