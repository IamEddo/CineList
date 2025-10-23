package com.example.cinelist.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_entries",
    foreignKeys = [ForeignKey(
        entity = Watchlist::class,
        parentColumns = ["id"],
        childColumns = ["watchlistId"],
        onDelete = ForeignKey.CASCADE // Se deletar a lista, deleta os filmes
    )]
)
data class MovieEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val watchlistId: Long, // Chave estrangeira
    val imdbId: String,
    val title: String,
    val year: String,
    val posterUrl: String,
    var userNotes: String = "" // Campo para o CRUD de Update
)