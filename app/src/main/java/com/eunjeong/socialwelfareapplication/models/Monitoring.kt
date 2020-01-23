package com.eunjeong.socialwelfareapplication.models

data class Monitoring(
    val key: String = "",
    val date: String = "",
    val writer: String = "",
    val image: String = "",
    val place: String = "",
    val purpose: String = "",
    val state: String = "",
    val remark: String = "",
    val comments: Boolean = false,
    var monitoringKey: String = "",
    val visitor: String = ""
)