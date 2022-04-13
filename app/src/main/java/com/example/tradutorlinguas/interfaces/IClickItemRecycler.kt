package com.example.tradutorlinguas.interfaces

import com.example.tradutorlinguas.dataclass.LanguageData

interface IClickItemRecycler {
    fun clickClose(id: Int, position: Int)
    fun clickBox(item: LanguageData)
}