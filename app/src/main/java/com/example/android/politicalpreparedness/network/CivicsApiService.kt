package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"
private const val API_KEY = ""

// TODO: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .add(ElectionAdapter::class.java)
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    //TODO: Add elections API Call
    @GET("/elections")
    suspend fun getElections(@Query("key") key: String = API_KEY)

    //TODO: Add voterinfo API Call
    @GET("/voterinfo")
    suspend fun getVoterInfo(@Query("electionId") electionId: Int, @Query("key") key: String = API_KEY)

    //TODO: Add representatives API Call
    @GET("/representatives")
    suspend fun getRepresentatives(@Query("address") address: String, @Query("key") key: String = API_KEY)
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}