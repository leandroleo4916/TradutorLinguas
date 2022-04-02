package com.example.tradutorlinguas.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tradutorlinguas.dataclass.LanguageData
import com.example.tradutorlinguas.remote.Translator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelApi(private val repository: Translator): ViewModel() {

    val translateValue = MutableLiveData<String>()

    fun translate(language: LanguageData){
        CoroutineScope(Dispatchers.Main).launch {
            val ret = withContext(Dispatchers.Default) {
                repository.translate(language)
            }
            translateValue.value = ret
        }
    }

    fun saveTranslate(){

    }

}