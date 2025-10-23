package com.example.cinelist.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinelist.data.MovieRepository
import com.example.cinelist.data.local.Watchlist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    // READ
    val watchlists = repository.getAllWatchlists()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // CREATE
    fun createWatchlist(name: String) {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                repository.addWatchlist(name)
            }
        }
    }

    // DELETE
    fun deleteWatchlist(watchlist: Watchlist) {
        viewModelScope.launch {
            repository.deleteWatchlist(watchlist)
        }
    }
}