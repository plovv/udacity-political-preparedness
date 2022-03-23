package com.example.android.politicalpreparedness.election

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.repository.Repository
import kotlinx.coroutines.launch
import java.lang.Exception

class VoterInfoViewModel(private val repo: Repository, private val electionID: Int, private val division: Division) : ViewModel() {

    private var _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private var _isSaved = MutableLiveData<Boolean>(false)
    val isSaved: LiveData<Boolean>
        get() = _isSaved

    private var _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private var _openUrl = MutableLiveData<Uri?>()
    val openUrl: LiveData<Uri?>
        get() = _openUrl

    private val _showError = MutableLiveData<String?>(null)
    val showError: LiveData<String?>
        get() = _showError
    fun errorShown() { _showError.value = null }

    init {
        viewModelScope.launch {
            checkForSavedElection()
            getVoterInfo()
        }
    }

    private suspend fun checkForSavedElection() {
        val savedElection = repo.getElection(electionID)

        savedElection?.run {
            _isSaved.value = true
            _election.value = this
        }
    }

    private suspend fun getVoterInfo() {
        try {
            val country = division.country
            val state = division.state
            val address = "${country},${state}"

            val voterInfo = repo.retrieveVoterInfo(address, electionID)

            _election.value = voterInfo.election
            _state.value = voterInfo.state?.get(0)
        } catch (e: Exception) {
            Log.i("VoterInfoViewModel", "Remote failed: ${e.message}")
            _showError.value = "Failed to retrieve voter info. Please check your internet connection"
        }
    }

    fun openWebLink(url: String) {
        val uri = Uri.parse(url)
        _openUrl.value = uri
    }

    fun clearOpenUrl() {
        _openUrl.value = null
    }

    fun saveElection(election: Election) {
        viewModelScope.launch {
            if (isSaved.value == true) {
                repo.deleteSavedElection(election.id)
                _isSaved.value = false
            } else {
                repo.saveElection(election)
                _isSaved.value = true
            }
        }
    }

}