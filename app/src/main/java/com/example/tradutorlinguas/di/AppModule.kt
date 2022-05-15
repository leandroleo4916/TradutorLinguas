package com.example.tradutorlinguas.di

import com.example.tradutorlinguas.activity.AnimatorView
import com.example.tradutorlinguas.activity.CreateMenu
import com.example.tradutorlinguas.activity.ModifyIcon
import com.example.tradutorlinguas.activity.ShowToast
import com.example.tradutorlinguas.dbhistory.DataBaseHistory
import com.example.tradutorlinguas.remote.PlayVoice
import com.example.tradutorlinguas.remote.Translator
import com.example.tradutorlinguas.repository.RepositoryHistory
import com.example.tradutorlinguas.util.*
import com.example.tradutorlinguas.viewmodel.ViewModelApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module { factory { Translator() } }
val viewModelModule = module { viewModel { ViewModelApi(get(), get()) } }
val playVoice = module { factory { PlayVoice() } }
val dataBase = module { single { DataBaseHistory(get()) } }
val repositoryHistory = module { single { RepositoryHistory(get()) } }
val captureHourDate = module { factory { CaptureHourDate() } }
val getFlag = module { factory { CaptureFlag() } }
val moduleCreateMenu = module { factory { CreateMenu() } }
val securityPreferences = module { factory { SecurityPreferences(get()) } }
val animatorImage = module { factory { AnimatorView() } }
val modifyIcon = module { factory { ModifyIcon() } }
val showToast = module { factory { ShowToast() } }
val formatText = module { factory { FormatText() } }

val appModules = listOf( repositoryModule, viewModelModule, playVoice, moduleCreateMenu,
        dataBase, repositoryHistory, captureHourDate, getFlag, securityPreferences, animatorImage,
        modifyIcon, showToast, formatText)