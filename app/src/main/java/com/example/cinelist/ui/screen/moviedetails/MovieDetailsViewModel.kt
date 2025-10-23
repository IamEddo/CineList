package com.example.cinelist.ui.screen.moviedetails

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinelist.data.MovieRepository
import com.example.cinelist.data.local.MovieEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Long = checkNotNull(savedStateHandle["movieId"])

    private val _movie = MutableStateFlow<MovieEntry?>(null)
    val movie = _movie.asStateFlow()

    var notes by mutableStateOf("")
        private set

    init {
        loadMovie()
    }

    private fun loadMovie() {
        viewModelScope.launch {
            val movieData = repository.getMovieById(movieId)
            _movie.value = movieData
            notes = movieData?.userNotes ?: ""
        }
    }

    fun onNotesChange(newNotes: String) {
        notes = newNotes
    }

    // UPDATE
    fun saveNotes() {
        _movie.value?.let {
            viewModelScope.launch {
                val updatedMovie = it.copy(userNotes = notes)
                repository.updateMovieNotes(updatedMovie)
                _movie.update { updatedMovie } // Atualiza o estado local
            }
        }
    }

    // SHARE (Requisito de Compartilhamento)
    fun shareMovie(context: Context) {
        _movie.value?.let {
            val shareText = """
                Ei, dá uma olhada neste filme que adicionei ao meu CineList!
                
                Título: ${it.title} (${it.year})
                Minhas Notas: ${it.userNotes}
            """.trimIndent()

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Compartilhar filme via...")
            context.startActivity(shareIntent)
        }
    }
}