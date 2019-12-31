package com.example.socialwelfareapplication.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.socialwelfareapplication.models.Notice
import com.example.socialwelfareapplication.models.storgeRef
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
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

    fun image(uri: String): StorageReference { return storgeRef.child("notice/$uri")}

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

        image?.let {
            storgeRef.child("notice/${notice.image}").putFile(image)
                .addOnSuccessListener { documentReference->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.uploadSessionUri}")

                }.addOnFailureListener { e->
                    e.printStackTrace()
                }
        }

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
