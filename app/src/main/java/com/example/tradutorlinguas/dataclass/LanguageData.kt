package com.example.tradutorlinguas.dataclass

data class LanguageData(
        val id: Int,
        val from: String,
        val to: String,
        val textFrom: String,
        val textTo: String,
        val hour: String,
        val date: String
)
