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

    private var menuList: List<Notice> = emptyList()
        set(value) {
            menuListPublisher.onNext(value)
        }

    private var currentMenu: Notice? = Notice()
        set(value) {
            value?.let { currentMenuPublisher.onNext(it) }
            field = value
        }

    var noticePublisher = PublishSubject.create<List<Notice>>()
    var menuListPublisher = PublishSubject.create<List<Notice>>()
    var currentMenuPublisher = PublishSubject.create<Notice>()
    private val db = Firebase.firestore

    fun addData(notice: Notice, image: Uri?, onSubscribe: (() -> Unit)? = null) {

        val ref = db.collection("notice").document()
        notice.key = ref.id
        ref.set(notice)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
                image?.let { saveImage("notice/${notice.image}", it) { getData(onSubscribe) } }
                    ?: getData(onSubscribe)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }


    }


    fun getData(onSubscribe: (() -> Unit)? = null) {

        db.collection("notice").whereEqualTo("group", "공지사항")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                noticeList = result.toObjects(Notice::class.java)
                onSubscribe?.invoke()

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }
    }

    fun getCurrentMenuData(date: String) {
        db.collection("notice").whereEqualTo("group", "도시락").whereEqualTo("date", date)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                currentMenu = result.toObjects(Notice::class.java).firstOrNull()

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }
    }

    fun getMenuData() {
        db.collection("notice").whereEqualTo("group", "도시락")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                menuList = result.toObjects(Notice::class.java)

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }
    }

    fun removeData(key: String, onSubscribe: (() -> (Unit))? = null) {
        db.collection("notice").document(key)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot remove")
                onSubscribe?.invoke()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }
    }

}
