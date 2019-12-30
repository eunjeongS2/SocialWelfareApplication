package com.example.socialwelfareapplication.models

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo

val auth = FirebaseAuth.getInstance()

class FirebaseProvider {

    private val db = Firebase.firestore

    companion object {
        const val TAG = "FirebaseProvider"
    }

    fun addData(path: String, data: Any) {

        db.collection(path)
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }


    fun getData(path: String) : QuerySnapshot? {

        var value: QuerySnapshot? = null

        db.collection(path)
            .get()
            .addOnSuccessListener { result ->
                value = result
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }

        return value

    }
}