package com.example.tradutorlinguas.util

import com.example.tradutorlinguas.R

class GetColor {

    fun getColor (position: Int): Int{

        return when(position%10){
            0 -> R.drawable.back_fundo_blue2
            1 -> R.drawable.back_fundo_orange
            2 -> R.drawable.back_fundo_pink
            3 -> R.drawable.back_fundo_green
            4 -> R.drawable.back_fundo_blue
            5 -> R.drawable.back_fundo_blue2
            6 -> R.drawable.back_fundo_orange
            7 -> R.drawable.back_fundo_pink
            8 -> R.drawable.back_fundo_green
            else -> R.drawable.back_fundo_blue
        }
    }
}