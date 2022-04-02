package com.example.tradutorlinguas.dataclass

data class LanguageData(
        val from: String,
        val to: String,
        val text: String,
)

data class LanguageSaveData(
        val from: String,
        val to: String,
        val textTo: String,
        val textFrom: String,
)
