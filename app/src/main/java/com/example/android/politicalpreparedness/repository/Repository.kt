package com.example.android.politicalpreparedness.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.*

class Repository(val app: Application) {

    private val db = ElectionDatabase.getInstance(app)
    private val remote = CivicsApi.retrofitService

    val elections = db.electionDao.getElections()

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            val remoteElections = remote.getElections()

            db.electionDao.deleteElections()
            db.electionDao.insertElections(remoteElections.elections)
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

    suspend fun updateElection(followElection: Election) {
        withContext(Dispatchers.IO) {
            db.electionDao.insertElections(listOf(
                followElection
            ))
        }
    }

    suspend fun retrieveRepresentatives(address: String): RepresentativeResponse {
        return withContext(Dispatchers.IO) {
            remote.getRepresentatives(address)
        }
    }

}