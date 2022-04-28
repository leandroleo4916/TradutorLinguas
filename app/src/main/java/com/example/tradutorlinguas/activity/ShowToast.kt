package com.example.tradutorlinguas.activity

import android.content.Context
import android.widget.Toast

class ShowToast {
    fun toast(message: String, context: Context){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}