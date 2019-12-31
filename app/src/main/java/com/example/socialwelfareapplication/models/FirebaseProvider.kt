package com.example.socialwelfareapplication.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

val auth = FirebaseAuth.getInstance()
val storage = Firebase.storage
val storgeRef = storage.reference
