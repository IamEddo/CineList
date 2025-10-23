package com.example.cinelist.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET("/")
    suspend fun searchMovies(
        @Query("s") searchTerm: String,
        @Query("apikey") apiKey: String,
        @Query("type") type: String = "movie"
    ): MovieSearchResponse
}