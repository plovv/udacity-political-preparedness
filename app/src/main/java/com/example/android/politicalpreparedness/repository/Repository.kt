package com.example.android.politicalpreparedness.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Repository(val app: Application) {

    private val db = ElectionDatabase.getInstance(app)
    private val remote = CivicsApi.retrofitService

    private val _upcomingElections = MutableLiveData<List<Election>?>()
    val upcomingElections: LiveData<List<Election>?>
        get() = _upcomingElections

    val savedElections = db.electionDao.getElections()

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            val result = remote.getElections()
            _upcomingElections.postValue(result.elections)
        }
    }

    suspend fun getElection(electionId: Int): Election {
        return withContext(Dispatchers.IO) {
            db.electionDao.getElectionById(electionId)
        }
    }

    suspend fun retrieveVoterInfo(address: String, electionId: Int): VoterInfoResponse {
        return withContext(Dispatchers.IO) {
            remote.getVoterInfo(address, electionId)
        }
    }

    suspend fun saveElection(election: Election) {
        withContext(Dispatchers.IO) {
            db.electionDao.insertElection(election)
        }
    }

    suspend fun deleteSavedElection(electionId: Int) {
        withContext(Dispatchers.IO) {
            db.electionDao.deleteElection(electionId)
        }
    }

    suspend fun retrieveRepresentatives(address: String): RepresentativeResponse {
        return withContext(Dispatchers.IO) {
            remote.getRepresentatives(address)
        }
    }

}