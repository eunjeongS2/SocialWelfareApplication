package com.example.socialwelfareapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.socialwelfareapplication.models.Monitoring
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.subjects.PublishSubject


class MonitoringViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "MonitoringViewModel"
    }

    var monitoringList: List<Monitoring> = emptyList()
        set(value) {
            monitoringPublisher.onNext(value)
        }

    var monitoringPublisher = PublishSubject.create<List<Monitoring>>()

    private val db = Firebase.firestore

    fun addData(monitoring: Monitoring) {

        db.collection("monitoring")
            .add(monitoring)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }


    fun getData(onSubscribe: (() -> Unit)? = null) {

        db.collection("monitoring")
            .get()
            .addOnSuccessListener { result ->
                monitoringList = result.toObjects(Monitoring::class.java)

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }

    }

}