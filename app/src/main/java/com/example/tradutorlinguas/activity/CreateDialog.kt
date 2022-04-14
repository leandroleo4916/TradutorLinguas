package com.example.tradutorlinguas.activity

import android.content.Context
import androidx.appcompat.app.AlertDialog

class CreateDialog {

    fun createDialog(title: String, context: Context): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setCancelable(false)
        return builder
    }
}