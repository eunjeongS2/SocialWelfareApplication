package com.example.socialwelfareapplication.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.socialwelfareapplication.models.Notice
import com.example.socialwelfareapplication.models.saveImage
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.subjects.PublishSubject

class NoticeViewModel : ViewModel() {

    companion object {
        const val TAG = "NoticeViewModel"
    }

    private var noticeList: List<Notice> = emptyList()
        set(value) {
            noticePublisher.onNext(value)
        }

    var noticePublisher = PublishSubject.create<List<Notice>>()
    private val db = Firebase.firestore

    fun addData(notice: Notice, image: Uri?, onSubscribe: (() -> Unit)? = null) {

        db.collection("notice")
            .add(notice)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                getData(onSubscribe)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        image?.let { saveImage("notice/${notice.image}", it) { getData(onSubscribe) } }

    }


    fun getData(onSubscribe: (() -> Unit)? = null) {

        db.collection("notice").orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                noticeList = result.toObjects(Notice::class.java)
                onSubscribe?.invoke()

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }

    }

}
