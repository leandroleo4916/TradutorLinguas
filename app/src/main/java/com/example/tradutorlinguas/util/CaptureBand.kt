package com.example.tradutorlinguas.util

import com.example.tradutorlinguas.R

class CaptureBand {
    fun capture(value: String): Int{
        return when(value){
            "Português" -> { R.drawable.brasil }
            "Inglês" -> { R.drawable.eua }
            "Francês" -> { R.drawable.france }
            "Italiano" -> { R.drawable.italy }
            "Espanhol" -> { R.drawable.espan }
            else -> R.drawable.ic_close
        }
    }
}