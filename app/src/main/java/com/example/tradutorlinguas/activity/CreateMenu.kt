package com.example.tradutorlinguas.activity

import android.content.Context
import android.widget.ImageView
import android.widget.PopupMenu
import com.example.tradutorlinguas.R

class CreateMenu {
    fun createMenu(context: Context, imageViewOption: ImageView): PopupMenu {
        val popMenu = PopupMenu(context, imageViewOption)
        popMenu.menuInflater.inflate(R.menu.option, popMenu.menu)
        return popMenu
    }
}