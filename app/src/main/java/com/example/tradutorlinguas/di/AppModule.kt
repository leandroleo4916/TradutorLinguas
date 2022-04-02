package com.example.tradutorlinguas.di

import com.example.tradutorlinguas.remote.PlayVoice
import com.example.tradutorlinguas.remote.Translator
import com.example.tradutorlinguas.viewmodel.ViewModelApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { Translator() }
}

val viewModelModule = module {
    viewModel { ViewModelApi(get()) }
}

val playVoice = module {
    factory { PlayVoice() }
}

val appModules = listOf( repositoryModule, viewModelModule, playVoice)