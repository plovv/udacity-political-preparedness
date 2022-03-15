package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(elections: List<Election>)

    //TODO: Add select all election query
    @Query("select * from election_table")
    fun getElections(): LiveData<List<Election>>

    //TODO: Add select single election query
    @Query("select * from election_table where id = :electionId")
    suspend fun getElectionById(electionId: Int): Election

    //TODO: Add delete query

    //TODO: Add clear query
    @Query("delete from election_table where following = 0")
    suspend fun deleteElections()

}