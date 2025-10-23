package com.example.cinelist.ui.screen.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinelist.data.MovieRepository
import com.example.cinelist.data.remote.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val results: List<SearchResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val listId: Long = checkNotNull(savedStateHandle["listId"])
    var uiState by mutableStateOf(SearchUiState())
        private set

    fun onQueryChange(newQuery: String) {
        uiState = uiState.copy(query = newQuery)
    }

    fun searchMovies() {
        if (uiState.query.isBlank()) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            val result = repository.searchMovies(uiState.query)
            uiState = result.fold(
                onSuccess = {
                    uiState.copy(isLoading = false, results = it)
                },
                onFailure = {
                    uiState.copy(isLoading = false, error = it.message, results = emptyList())
                }
            )
        }
    }

    // CREATE (Adicionar filme da API para o Room)
    fun addMovieToWatchlist(movie: SearchResult) {
        viewModelScope.launch {
            repository.addMovieToWatchlist(movie, listId)
        }
    }
}