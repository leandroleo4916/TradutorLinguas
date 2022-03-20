package com.example.tradutorlinguas.di

import org.koin.dsl.module
val repositoryModule = module {
    //single { TranslateRepository(get()) }
}

val viewModelModule = module {
    //viewModel { ViewModelApi(get()) }
}

val appModules = listOf( repositoryModule, viewModelModule)