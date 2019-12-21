package com.example.socialwelfareapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.socialwelfareapplication.models.Contact
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.subjects.PublishSubject

class UserViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "UserViewModel"
    }

    private val db = Firebase.firestore

    var userList: List<Contact> = emptyList()
        set(value) {
            userPublisher.onNext(value)
        }

    var group: String = "어르신"
        set(value) {
           getData(value)
        }

    var userPublisher = PublishSubject.create<List<Contact>>()


    fun addData(path: String, contact: Contact) {

        db.collection(path).document(contact.phoneNumber)
            .set(contact)
            .addOnSuccessListener { documentReference ->
                Log.d(MonitoringViewModel.TAG, "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w(MonitoringViewModel.TAG, "Error adding document", e)
            }
    }


    fun getData(path: String) {

        db.collection(path)
            .get()
            .addOnSuccessListener { result ->
                userList = result.toObjects(Contact::class.java)


            }
            .addOnFailureListener { e ->
                Log.w(MonitoringViewModel.TAG, "Error getting documents.", e)

            }

    }
}
