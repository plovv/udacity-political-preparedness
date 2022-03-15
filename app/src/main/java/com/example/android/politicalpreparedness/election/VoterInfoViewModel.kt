package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.repository.Repository
import kotlinx.coroutines.launch

class VoterInfoViewModel(val repo: Repository, private val electionID: Int, private val division: Division) : ViewModel() {

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

    init {
        viewModelScope.launch {
            _election.value = repo.getElection(electionID)

            var address = ""
            division.apply {
                address = "${this.country},${this.state}"
            }
            val voterInfo = repo.retrieveVoterInfo(address, electionID)

            _state.value = voterInfo.state
        }
    }

}