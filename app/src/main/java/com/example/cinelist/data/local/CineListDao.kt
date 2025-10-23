package com.example.cinelist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CineListDao {

    // --- CRUD Watchlist ---
    @Insert
    suspend fun insertWatchlist(watchlist: Watchlist)

    @Update
    suspend fun updateWatchlist(watchlist: Watchlist)

    @Delete
    suspend fun deleteWatchlist(watchlist: Watchlist)

    @Query("SELECT * FROM watchlists ORDER BY name ASC")
    fun getAllWatchlists(): Flow<List<Watchlist>>

    // --- CRUD MovieEntry ---
    @Insert
    suspend fun insertMovie(movieEntry: MovieEntry)

    @Update
    suspend fun updateMovie(movieEntry: MovieEntry) // Usado para salvar notas

    @Delete
    suspend fun deleteMovie(movieEntry: MovieEntry)

    @Query("SELECT * FROM movie_entries WHERE watchlistId = :listId ORDER BY title ASC")
    fun getMoviesForWatchlist(listId: Long): Flow<List<MovieEntry>>

    @Query("SELECT * FROM movie_entries WHERE id = :movieId")
    suspend fun getMovieById(movieId: Long): MovieEntry?
}