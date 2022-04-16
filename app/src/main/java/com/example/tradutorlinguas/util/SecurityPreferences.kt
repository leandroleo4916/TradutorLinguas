package com.example.tradutorlinguas.util

import android.content.Context
import android.content.SharedPreferences

class SecurityPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "EnableOrDisableView", Context.MODE_PRIVATE)

    fun putStoreString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getStoredString(key: String): String? {
        return sharedPreferences.getString(key,"")
    }

    fun removeEnable(){
        sharedPreferences.edit().run {
            remove("enable").apply()
        }
    }
    fun removeDisable(){
        sharedPreferences.edit().run {
            remove("disable").apply()
        }
    }
}