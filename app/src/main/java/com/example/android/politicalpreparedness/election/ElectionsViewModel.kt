package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.Repository
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(val repo: Repository): ViewModel() {

    //TODO: Create live data val for upcoming elections
    private var _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    //TODO: Create live data val for saved elections
    private var _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    init {
        Transformations.map(repo.elections) { elections ->
            _upcomingElections.value = elections
            _savedElections.value = elections?.filter { it.following }
        }

        viewModelScope.launch {
            repo.refreshElections()
        }
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
    private var _navigateToVoterInfo = MutableLiveData<Pair<Int, Division>?>()
    val navigateToVoterInfo: LiveData<Pair<Int, Division>?>
        get() = _navigateToVoterInfo
    fun completeNavigation() { _navigateToVoterInfo.value = null }

    fun onElectionSelect(election: Election) {
        election.division.
    }

}