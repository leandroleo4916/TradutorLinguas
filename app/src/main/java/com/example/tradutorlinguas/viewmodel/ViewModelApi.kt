package com.example.tradutorlinguas.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tradutorlinguas.dataclass.LanguageData
import com.example.tradutorlinguas.di.repositoryHistory
import com.example.tradutorlinguas.remote.Translator
import com.example.tradutorlinguas.repository.RepositoryHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelApi(private val translate: Translator,
                   private val repositoryHistory: RepositoryHistory): ViewModel() {

    val translateValue = MutableLiveData<String>()
    val history = MutableLiveData<ArrayList<LanguageData>>()

    fun translate(language: LanguageData){
        CoroutineScope(Dispatchers.Main).launch {
            val ret = withContext(Dispatchers.Default) {
                translate.translate(language)
            }
            translateValue.value = ret
        }
    }

    fun saveHistory(history: LanguageData): Boolean{
        return repositoryHistory.saveEmployee(history)
    }

    fun consultHistory(){
        history.value =  repositoryHistory.historyList()
    }

}