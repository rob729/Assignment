package com.robin729.assignment.util

import android.content.Context
import android.content.SharedPreferences

object StoreSession {

    lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences =
            context.getSharedPreferences("Assign_shared_pref", Context.MODE_PRIVATE)
    }

    fun writeFloat(key: String, value: Float) {
        sharedPreferences.edit().let {
            it.putFloat(key, value)
            it.apply()
        }
    }

    fun readFloat(key: String): Float = sharedPreferences.getFloat(key, 0.0000645f)
}