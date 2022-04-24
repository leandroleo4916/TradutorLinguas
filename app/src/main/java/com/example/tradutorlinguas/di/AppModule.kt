package com.example.tradutorlinguas.di

import com.example.tradutorlinguas.activity.CreateMenu
import com.example.tradutorlinguas.dbhistory.DataBaseHistory
import com.example.tradutorlinguas.remote.PlayVoice
import com.example.tradutorlinguas.remote.Translator
import com.example.tradutorlinguas.repository.RepositoryHistory
import com.example.tradutorlinguas.util.CaptureFlag
import com.example.tradutorlinguas.util.CaptureHourDate
import com.example.tradutorlinguas.util.GetColor
import com.example.tradutorlinguas.util.SecurityPreferences
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
val dataBase = module {
    single { DataBaseHistory(get()) }
}
val repositoryHistory = module {
    factory { RepositoryHistory(get()) }
}
val captureHourDate = module {
    factory { CaptureHourDate() }
}
val getColor = module {
    factory { GetColor() }
}
val getFlag = module {
    factory { CaptureFlag() }
}
val moduleCreateMenu = module {
    factory { CreateMenu() }
}
val securityPreferences = module {
    factory { SecurityPreferences(get()) }
}
val appModules = listOf( repositoryModule, viewModelModule, playVoice, moduleCreateMenu,
        dataBase, repositoryHistory, captureHourDate, getColor, getFlag, securityPreferences)