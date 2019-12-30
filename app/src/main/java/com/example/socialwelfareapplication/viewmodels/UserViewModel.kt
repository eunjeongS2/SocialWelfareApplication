package com.example.socialwelfareapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.socialwelfareapplication.models.Contact
import com.example.socialwelfareapplication.models.Monitoring
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.subjects.PublishSubject

class UserViewModel : ViewModel() {
    companion object {
        const val TAG = "UserViewModel"
    }

    private val db = Firebase.firestore

    private var userList: List<Contact> = emptyList()
        set(value) {
            userPublisher.onNext(value)
        }

    var group: String = "어르신"
        set(value) {
            getData(value)
        }

    var selectList: MutableList<Contact> = mutableListOf()
    private var monitoringList: List<Monitoring> = emptyList()
        set(value) {
            monitoringPublisher.onNext(value)
        }

    var userPublisher = PublishSubject.create<List<Contact>>()
    var monitoringPublisher = PublishSubject.create<List<Monitoring>>()


    fun addData(contact: Contact) {

        val ref = db.collection("contact").document()
        contact.key = ref.id
        ref.set(contact)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
                getData(contact.group)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }

    fun updateDate(key: String, group: String, item: Map<String, Any>) {
        db.collection("contact").document(key)
            .update(item)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
                getData(group)

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }

    private fun getData(path: String) {

        when (path) {
            "전체" -> {
                db.collection("contact")
                    .get()
                    .addOnSuccessListener { result ->
                        userList = result.toObjects(Contact::class.java)

                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error getting documents.", e)

                    }

            }
            "중요" -> {
                db.collection("contact")
                    .whereEqualTo("star", true)
                    .get()
                    .addOnSuccessListener { result ->
                        userList = result.toObjects(Contact::class.java)

                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error getting documents.", e)

                    }

            }
            else -> db.collection("contact").whereEqualTo("group", path)
                .get()
                .addOnSuccessListener { result ->
                    userList = result.toObjects(Contact::class.java)


                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error getting documents.", e)

                }
        }

    }

    fun getMonitoringString(key: String) {
        db.collection("monitoring")
            .whereEqualTo("key", key)
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(2)
            .get()
            .addOnSuccessListener { result ->
                monitoringList = result.toObjects(Monitoring::class.java)

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }
    }

    fun getMonitoringList(key: String) {
        db.collection("monitoring")
            .whereEqualTo("key", key)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                monitoringList = result.toObjects(Monitoring::class.java)

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents.", e)

            }
    }
}
