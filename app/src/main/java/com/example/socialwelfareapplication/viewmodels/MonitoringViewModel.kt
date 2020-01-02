package com.example.socialwelfareapplication.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.socialwelfareapplication.models.Monitoring
import com.example.socialwelfareapplication.models.saveImage
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject


class MonitoringViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "MonitoringViewModel"
    }

    private val disposeBag = CompositeDisposable()

    private var monitoringList: List<Monitoring> = emptyList()
        set(value) {
            monitoringPublisher.onNext(value)
        }

    var dateList: List<String> = emptyList()

    var monitoringPublisher = PublishSubject.create<List<Monitoring>>()

    private val db = Firebase.firestore

    fun addData(monitoring: Monitoring, image: Uri?, onSubscribe: ((List<Monitoring>) -> Unit)? = null) {

        db.collection("monitoring")
            .add(monitoring)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

                image?.let { saveImage("monitoring/${monitoring.image}", it) { getData(onSubscribe) } }
                    ?: getData(onSubscribe)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }


    fun getData(onSubscribe: ((List<Monitoring>) -> Unit)? = null) {

        db.collection("monitoring").orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                monitoringList = result.toObjects(Monitoring::class.java)
                dateList = result.toObjects(Monitoring::class.java).map { it.date }

                onSubscribe?.invoke(result.toObjects(Monitoring::class.java))

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }

    }

    fun filter(date: List<String>) {
        if (date.isEmpty()) getData()
        else getData {
            monitoringList = it.filter { monitoring ->
                date.toSet().contains(monitoring.date)
            }
        }
    }

    override fun onCleared() {
        disposeBag.dispose()
        super.onCleared()
    }

}