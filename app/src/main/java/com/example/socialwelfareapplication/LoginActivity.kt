package com.example.socialwelfareapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var id: String
    private lateinit var password: String

    companion object {
        const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        id = application.sharedPreference.id ?: ""
        password = application.sharedPreference.password ?: ""

        progressBar.visibility = View.INVISIBLE

        loginButton.setOnClickListener {

            if (emailText.text.toString() != "" && passwordText.text.toString() != "") {
                auth.signIn(emailText.text.toString(), passwordText.text.toString()) {
                    application.sharedPreference.id = emailText.text.toString()
                    application.sharedPreference.password = passwordText.text.toString()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser!=null) {
            changeActivity()

        } else if (id != "" && password != "") {
            auth.signIn(id, password) {
                emailText.setText(id)
                passwordText.setText(password)
                progressBar.visibility = View.VISIBLE
            }

        }
    }

    private fun FirebaseAuth.signIn(id: String, password: String, onSuccess: (()->Unit) ?= null){
        this.signInWithEmailAndPassword(id, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    onSuccess?.invoke()
                    changeActivity()

                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                }
            }

    }

    private fun changeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

}