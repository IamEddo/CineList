package com.example.cinelist.data.remote

import com.google.gson.annotations.SerializedName

// Resposta geral da API
data class MovieSearchResponse(
    @SerializedName("Search")
    val Search: List<SearchResult> = emptyList(),
    @SerializedName("totalResults")
    val totalResults: String?,
    @SerializedName("Response")
    val Response: String
)

// Cada item de filme na busca
data class SearchResult(
    @SerializedName("Title")
    val Title: String,
    @SerializedName("Year")
    val Year: String,
    @SerializedName("imdbID")
    val imdbID: String,
    @SerializedName("Type")
    val Type: String,
    @SerializedName("Poster")
    val Poster: String
)