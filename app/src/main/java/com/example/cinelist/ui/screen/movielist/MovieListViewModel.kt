package com.example.cinelist.ui.screen.movielist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinelist.data.MovieRepository
import com.example.cinelist.data.local.MovieEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val listId: Long = checkNotNull(savedStateHandle["listId"])
    val listName: String = checkNotNull(savedStateHandle["listName"])

    // READ
    val movies = repository.getMoviesForWatchlist(listId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // DELETE
    fun deleteMovie(movie: MovieEntry) {
        viewModelScope.launch {
            repository.deleteMovie(movie)
        }
    }
}