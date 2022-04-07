package com.example.tradutorlinguas.util

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

class CaptureHourDate {

    private val local = Locale("pt", "BR")
    private val date = SimpleDateFormat("dd/MM/yyyy", local)
    private val hora = SimpleDateFormat("HH:mm", local)

    fun captureDateCurrent(): String{
        val calendar = Calendar.getInstance().time
        return date.format(calendar)
    }

    fun captureHoraCurrent(): String{
        val calendar = Calendar.getInstance().time
        return hora.format(calendar)
    }
}