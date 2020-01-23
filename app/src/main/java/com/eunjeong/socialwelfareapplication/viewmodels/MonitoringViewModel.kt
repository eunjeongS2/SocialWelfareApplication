package com.eunjeong.socialwelfareapplication.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.eunjeong.socialwelfareapplication.models.Monitoring
import com.eunjeong.socialwelfareapplication.models.removeImage
import com.eunjeong.socialwelfareapplication.models.saveImage
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

    fun addData(monitoring: Monitoring, image: Uri?, onSubscribe: (() -> Unit)? = null) {

        val ref = db.collection("monitoring").document()
        monitoring.monitoringKey = ref.id

        ref.set(monitoring)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")

                image?.let { saveImage("monitoring/${monitoring.image}", it) { onSubscribe?.invoke() } }
                    ?: onSubscribe?.invoke()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }


    fun getData(onSubscribe: ((List<Monitoring>) -> Unit)? = null) {

        db.collection("monitoring").orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->

                if(onSubscribe == null) {
                    monitoringList = result.toObjects(Monitoring::class.java)
                    dateList = result.toObjects(Monitoring::class.java).map { it.date }
                } else {
                    onSubscribe.invoke(result.toObjects(Monitoring::class.java))

                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }

    }

    fun filter(date: List<String>, onSubscribe: ((List<Monitoring>) -> Unit)? = null) {

        if (date.isEmpty()) getData()
        else getData {

            val filterList = it.filter { monitoring ->
                date.toSet().contains(monitoring.date)
            }

            if (onSubscribe != null) {
                onSubscribe.invoke(filterList)
            } else {
                monitoringList = filterList
            }
        }
    }

    fun removeData(key: String, image:String, onSubscribe: (() -> (Unit))? = null) {
        db.collection("monitoring").document(key)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot remove")

                if (image != "") {
                    removeImage("monitoring/$image")
                }

                onSubscribe?.invoke()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }
    }

    override fun onCleared() {
        disposeBag.dispose()
        super.onCleared()
    }

}