package com.eunjeong.socialwelfareapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.eunjeong.socialwelfareapplication.models.Comment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.subjects.PublishSubject

class CommentViewModel : ViewModel() {

    companion object {
        const val TAG = "CommentViewModel"
    }

    private var commentList : List<Comment> = emptyList()
        set(value) {
            commentPublisher.onNext(value)
        }

    var commentPublisher = PublishSubject.create<List<Comment>>()

    private val db = Firebase.firestore


    fun getCommentCount(key: String, onSubscribe: ((String) -> Unit)? = null) {
        db.collection("comment").document(key)
            .get()
            .addOnSuccessListener { result ->
                onSubscribe?.invoke(result["count"].toString())
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }
    }

    fun getData(key: String) {
        db.collection("comment").document(key).collection("comments")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                commentList = result.toObjects(Comment::class.java)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }
    }

    fun addData(key: String, comment: Comment) {
        db.collection("comment").document(key).collection("comments").document()
            .set(comment)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
                addCount(key)
                getData(key)

            }.addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)
            }
    }

    private fun addCount(key: String) {
        getCommentCount(key) {
            val value: String = if(it == "null") {
                "1"
            } else {
                (Integer.parseInt(it)+1).toString()
            }

            db.collection("comment").document(key)
                .set(mapOf("count" to value))
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
                    updateMonitoring(key)

                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error getting documents.", e)
                }

        }
    }

    private fun updateMonitoring(key: String) {
        db.collection("monitoring").document(key)
            .update(mapOf("comments" to true))
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")

            }.addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)
            }
    }
}