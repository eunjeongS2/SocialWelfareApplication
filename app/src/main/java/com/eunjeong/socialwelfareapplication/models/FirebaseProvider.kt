package com.eunjeong.socialwelfareapplication.models

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

val auth = FirebaseAuth.getInstance()
val storage = Firebase.storage
val storageRef = storage.reference

const val TAG = "FirebaseProvider"

fun saveImage(path: String, image: Uri, onSubscribe: (() -> Unit)? = null) {
    storageRef.child(path).putFile(image)
        .addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.uploadSessionUri}")
            onSubscribe?.invoke()

        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
}

fun updateImage(path: String, image: Uri, previous: String, onSubscribe: (() -> Unit)? = null) {
    saveImage(path, image, onSubscribe.also { removeImage(previous) })

}

fun imageReference(uri: String): StorageReference {
    return storageRef.child(uri)
}

fun removeImage(path: String) {
    storageRef.child(path).delete().addOnSuccessListener {

    }.addOnFailureListener {e ->
        e.printStackTrace()
    }
}

