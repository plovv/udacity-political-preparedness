package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.Repository
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(val repo: Repository): ViewModel() {

    //TODO: Create live data val for upcoming elections
    val upcomingElections = repo.elections

    //TODO: Create live data val for saved elections
    val savedElections =  Transformations.map(repo.elections) { elections ->
        elections?.filter { it.following }
    }

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    init {
        viewModelScope.launch {
            try {
                repo.refreshElections()
            } catch (e: Exception) {
                // show toast
                Log.i("ElectionsViewModel", "failed in vm ${e.message}")
            }
        }
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
    private var _navigateToVoterInfo = MutableLiveData<Election?>()
    val navigateToVoterInfo: LiveData<Election?>
        get() = _navigateToVoterInfo
    fun completeNavigation() { _navigateToVoterInfo.value = null }

    fun onElectionSelect(election: Election) {
        _navigateToVoterInfo.value = election
    }

}