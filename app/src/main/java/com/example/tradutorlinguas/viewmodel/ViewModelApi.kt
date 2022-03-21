package com.example.tradutorlinguas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tradutorlinguas.dataclass.LanguageData
import com.example.tradutorlinguas.remote.Resultado
import com.example.tradutorlinguas.remote.TranslateRepository

class ViewModelApi(private val repository: TranslateRepository): ViewModel() {

    //fun translate(language: LanguageData): LiveData<Resultado<String?>> = repository.translate(language)
}