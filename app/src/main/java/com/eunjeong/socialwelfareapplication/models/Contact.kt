package com.eunjeong.socialwelfareapplication.models

data class Contact(
    var key: String = "",
    val group: String = "",
    val name: String = "",
    val image: String = "",
    val phoneNumber: String = "",
    val emergencyNumber: String = "",
    val remark: String = "",
    val address: String = "",
    val star: Boolean = false
)

