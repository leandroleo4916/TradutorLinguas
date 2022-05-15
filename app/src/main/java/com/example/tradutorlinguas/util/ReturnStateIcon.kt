package com.example.tradutorlinguas.util

import android.widget.ImageView
import com.example.tradutorlinguas.R
import com.example.tradutorlinguas.di.modifyIcon
import com.example.tradutorlinguas.interfaces.IModifyValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*

class ReturnStateIcon(private val modifyValue: IModifyValue) {

    private val timer = Timer()

    fun returnStateIcon(interval: Long, icon: ImageView){

        val delay = interval*80
        timer.schedule(object : TimerTask() {
            override fun run() {
                CoroutineScope(Dispatchers.Main).launch {
                    coroutineScope {
                        icon.setImageResource(R.drawable.ic_sound)
                        modifyValue.modifyValue(0)
                    }
                }
            }
        }, delay)
    }
}