package com.example.socialwelfareapplication

fun String.checkDate(): String {

    return if (this.length == 1) {
        "0$this"
    } else this

}