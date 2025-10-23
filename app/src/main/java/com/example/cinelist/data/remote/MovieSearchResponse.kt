package com.example.cinelist.data.remote

import com.google.gson.annotations.SerializedName

// Resposta geral da API
data class MovieSearchResponse(
    // CORREÇÃO: Propriedade renomeada para 'search' (minúsculo)
    @SerializedName("Search")
    val search: List<SearchResult> = emptyList(),

    @SerializedName("totalResults")
    val totalResults: String?,

    // CORREÇÃO: Propriedade renomeada para 'response' (minúsculo)
    @SerializedName("Response")
    val response: String
)

// Cada item de filme na busca
data class SearchResult(
    // CORREÇÃO: Propriedade renomeada para 'title' (minúsculo)
    @SerializedName("Title")
    val title: String,

    // CORREÇÃO: Propriedade renomeada para 'year' (minúsculo)
    @SerializedName("Year")
    val year: String,

    @SerializedName("imdbID")
    val imdbID: String,

    // CORREÇÃO: Propriedade renomeada para 'type' (minúsculo)
    @SerializedName("Type")
    val type: String,

    // CORREÇÃO: Propriedade renomeada para 'poster' (minúsculo)
    @SerializedName("Poster")
    val poster: String
)
