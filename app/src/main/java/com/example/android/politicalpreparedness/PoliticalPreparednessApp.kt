package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.repository.Repository
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PoliticalPreparednessApp: Application() {

    private val appModule = module {
        single { Repository(get()) }

        viewModel {
            ElectionsViewModel(get())
            VoterInfoViewModel(get())
            RepresentativeViewModel(get())
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PoliticalPreparednessApp)
            modules(appModule)
        }
    }

}