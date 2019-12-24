package com.example.socialwelfareapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.socialwelfareapplication.models.Notice
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.subjects.PublishSubject

class NoticeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "NoticeViewModel"
    }

    var noticeList: List<Notice> = emptyList()
        set(value) {
            noticePublisher.onNext(value)
        }

    var noticePublisher = PublishSubject.create<List<Notice>>()
    private val db = Firebase.firestore


    fun addData(notice: Notice, onSubscribe: (() -> Unit)? = null) {

        db.collection("notice")
            .add(notice)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                onSubscribe?.invoke()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                onSubscribe?.invoke()
            }
    }


    fun getData(onSubscribe: (() -> Unit)? = null) {

        db.collection("notice").orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                noticeList = result.toObjects(Notice::class.java)

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }

    }

}