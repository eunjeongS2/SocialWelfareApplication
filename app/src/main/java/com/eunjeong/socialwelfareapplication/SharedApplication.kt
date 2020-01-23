package com.eunjeong.socialwelfareapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.jakewharton.threetenabp.AndroidThreeTen

class SharedApplication : Application() {

    var id: String?
        get() {
            return preferences.getString(PREFERENCE_USER_ID, null) ?: return null
        }
        set(value) {
            if (value != null) {
                preferences.edit().putString(PREFERENCE_USER_ID, value).apply()
            }

        }
    var password: String?
        get() {
            return preferences.getString(PREFERENCE_USER_PASSWORD, null) ?: return null
        }
        set(value) {
            if (value != null) {
                preferences.edit().putString(PREFERENCE_USER_PASSWORD, value).apply()
            }
        }

    companion object {
        const val PREFERENCE_DOCUMENT_NAME = "com.example.socialwelfareapplication"
        const val PREFERENCE_USER_ID = "user_id"
        const val PREFERENCE_USER_PASSWORD = "user_password"

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

    }

    private val preferences: SharedPreferences
        get() = getSharedPreferences(PREFERENCE_DOCUMENT_NAME, Context.MODE_PRIVATE)
}

val Context.sharedPreference: SharedApplication
    get() = applicationContext as SharedApplication
