package com.example.tradutorlinguas.di

import com.example.tradutorlinguas.adapter.AdapterHistory
import com.example.tradutorlinguas.dbhistory.DataBaseHistory
import com.example.tradutorlinguas.remote.PlayVoice
import com.example.tradutorlinguas.remote.Translator
import com.example.tradutorlinguas.repository.RepositoryHistory
import com.example.tradutorlinguas.viewmodel.ViewModelApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    factory { Translator() }
}
val viewModelModule = module {
    viewModel { ViewModelApi(get(), get()) }
}
val playVoice = module {
    factory { PlayVoice() }
}
val adapterHistory = module {
    factory { AdapterHistory() }
}
val dataBase = module {
    single { DataBaseHistory(get()) }
}
val repositoryHistory = module {
    factory { RepositoryHistory(get()) }
}
val appModules = listOf( repositoryModule, viewModelModule, playVoice,
        adapterHistory, dataBase, repositoryHistory)