package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert
    suspend fun insertElection(election: Election)

    @Query("select * from election_table")
    fun getElections(): LiveData<List<Election>>

    @Query("select * from election_table where id = :electionId")
    suspend fun getElectionById(electionId: Int): Election

    @Query("delete from election_table where id = :electionId")
    suspend fun deleteElection(electionId: Int)

}