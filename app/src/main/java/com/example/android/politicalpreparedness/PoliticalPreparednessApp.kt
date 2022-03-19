package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.repository.Repository
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class PoliticalPreparednessApp: Application() {

    private val appModule = module {
        single { Repository(get()) }
        viewModel { ElectionsViewModel(get()) }
        viewModel { RepresentativeViewModel(get()) }
        viewModel {
                (electionID: Int, division: Division) -> VoterInfoViewModel(get(), electionID, division)
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@PoliticalPreparednessApp)
            modules(appModule)
        }
    }

}