package com.eunjeong.socialwelfareapplication

import android.content.Context
import android.net.Uri
import com.mlsdev.rximagepicker.Sources

fun String.checkDate(): String {

    return if (this.length == 1) {
        "0$this"
    } else this

}

fun Pair<Sources, Uri>.removeImage(context: Context) {
    if (this.first == Sources.CAMERA) {
        context.contentResolver?.delete(this.second, null, null)
    }
}