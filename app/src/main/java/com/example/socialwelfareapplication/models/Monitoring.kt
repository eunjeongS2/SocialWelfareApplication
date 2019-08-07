package com.example.socialwelfareapplication.models

data class Monitoring(
    val date: String,
    val writer: String,
    val place: String,
    val purpose: String,
    val state: String,
    val remark: String,
    val comments: Int
)