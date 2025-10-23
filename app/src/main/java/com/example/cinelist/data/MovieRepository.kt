package com.example.cinelist.data

import com.example.cinelist.data.local.CineListDao
import com.example.cinelist.data.local.MovieEntry
import com.example.cinelist.data.local.Watchlist
import com.example.cinelist.data.remote.OmdbApi
import com.example.cinelist.data.remote.SearchResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val dao: CineListDao,
    private val api: OmdbApi,
    private val apiKey: String
) {

    // --- API ---
    suspend fun searchMovies(title: String): Result<List<SearchResult>> {
        return try {
            val response = api.searchMovies(searchTerm = title, apiKey = apiKey)
            if (response.Response == "True") {
                Result.success(response.Search)
            } else {
                Result.failure(Exception("Filme não encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Watchlist CRUD ---
    fun getAllWatchlists() = dao.getAllWatchlists()
    suspend fun addWatchlist(name: String) = dao.insertWatchlist(Watchlist(name = name))
    suspend fun deleteWatchlist(watchlist: Watchlist) = dao.deleteWatchlist(watchlist)

    // --- MovieEntry CRUD ---
    fun getMoviesForWatchlist(listId: Long) = dao.getMoviesForWatchlist(listId)
    suspend fun getMovieById(movieId: Long) = dao.getMovieById(movieId)
    suspend fun deleteMovie(movieEntry: MovieEntry) = dao.deleteMovie(movieEntry)
    suspend fun updateMovieNotes(movieEntry: MovieEntry) = dao.updateMovie(movieEntry)

    suspend fun addMovieToWatchlist(movie: SearchResult, listId: Long) {
        val entry = MovieEntry(
            watchlistId = listId,
            imdbId = movie.imdbID,
            title = movie.Title,
            year = movie.Year,
            posterUrl = movie.Poster
        )
        dao.insertMovie(entry)
    }
}