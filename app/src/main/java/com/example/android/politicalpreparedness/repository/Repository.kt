package com.example.android.politicalpreparedness.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election

class Repository(val app: Application): Observer<List<Election>> {

    private val db = ElectionDatabase.getInstance(app)
    private val remote = CivicsApi.retrofitService

    private val elections: LiveData<List<Election>> by lazy {
        db.electionDao.getElections()
    }

    private var _currentElections = MutableLiveData<List<Election>>()
    val currentElections: LiveData<List<Election>>
        get() = _currentElections

    private var _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    init {
        elections.observeForever(this)
    }

    override fun onChanged(elections: List<Election>?) {
        if (elections != null) {
            _currentElections.value = elections!!
            _savedElections.value = elections!!.filter { election -> election.following }
        }
    }

    suspend fun refreshElections() {
        val remoteElections = remote.getElections()
    }

}