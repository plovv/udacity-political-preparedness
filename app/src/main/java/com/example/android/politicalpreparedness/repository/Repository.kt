package com.example.android.politicalpreparedness.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class Repository(val app: Application) {

    private val db = ElectionDatabase.getInstance(app)
    private val remote = CivicsApi.retrofitService

    val elections: LiveData<List<Election>> = db.electionDao.getElections()

    suspend fun refreshElections() {
        try {
            val remoteElections = remote.getElections()

            db.electionDao.deleteElections()
            db.electionDao.insertElections(remoteElections.elections)
        } catch (e: Exception) {
            // api error or db
        }
    }

    suspend fun getElection(electionId: Int): Election {
        return db.electionDao.getElectionById(electionId)
    }

    suspend fun retrieveVoterInfo(address: String, electionId: Int): VoterInfoResponse {
        return remote.getVoterInfo(address, electionId)
    }

}