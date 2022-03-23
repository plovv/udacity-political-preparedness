package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.Repository
import kotlinx.coroutines.launch


class ElectionsViewModel(val repo: Repository): ViewModel() {

    val upcomingElections = repo.upcomingElections
    val savedElections =  repo.savedElections

    private val _showError = MutableLiveData<String?>(null)
    val showError: LiveData<String?>
        get() = _showError
    fun errorShown() { _showError.value = null }

    private var _navigateToVoterInfo = MutableLiveData<Election?>()
    val navigateToVoterInfo: LiveData<Election?>
        get() = _navigateToVoterInfo
    fun completeNavigation() { _navigateToVoterInfo.value = null }

    init {
        viewModelScope.launch {
            try {
                repo.refreshElections()
            } catch (e: Exception) {
                Log.i("ElectionsViewModel", "Failed to refresh ${e.message}")
                _showError.value = "Failed to refresh elections, please check your internet connection."
            }
        }
    }

    fun onElectionSelect(election: Election) {
        _navigateToVoterInfo.value = election
    }

}