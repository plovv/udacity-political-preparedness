package com.example.android.politicalpreparedness.election

import android.net.Uri
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

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    private var _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private var _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private var _openUrl = MutableLiveData<Uri?>()
    val openUrl: LiveData<Uri?>
        get() = _openUrl

    init {
        viewModelScope.launch {
            _election.value = repo.getElection(electionID)
            getVoterInfo()
        }
    }

    private suspend fun getVoterInfo() {
        try {
            val country = division.country
            val state = division.state
            val address = "${country},${state}"

            val voterInfo = repo.retrieveVoterInfo(address, electionID)

            _state.value = voterInfo.state?.get(0)
        } catch (e: Exception) {
            // failed
        }
    }

    fun openWebLink(url: String) {
        val uri = Uri.parse(url)
        _openUrl.value = uri
    }

    fun clearOpenUrl() {
        _openUrl.value = null
    }

    fun followElection(followElection: Election) {
        followElection.following = !followElection.following

        viewModelScope.launch {
            repo.updateElection(followElection)
            _election.value = repo.getElection(followElection.id)
        }
    }

}