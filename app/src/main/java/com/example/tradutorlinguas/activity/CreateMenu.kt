package com.example.tradutorlinguas.activity

import android.content.Context
import android.widget.ImageView
import android.widget.PopupMenu
import com.example.tradutorlinguas.R

class CreateMenu {
    fun createMenu(context: Context, imageViewOption: ImageView, menu: Int): PopupMenu {
        val popMenu = PopupMenu(context, imageViewOption)
        popMenu.menuInflater.inflate(menu, popMenu.menu)
        return popMenu
    }
}