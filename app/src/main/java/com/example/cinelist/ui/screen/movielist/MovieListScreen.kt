package com.example.cinelist.ui.screen.movielist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cinelist.data.local.MovieEntry
import com.example.cinelist.ui.screen.movielist.MovieListViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
    listId: Long,
    listName: String,
    onNavigateToSearch: (Long) -> Unit,
    onNavigateToDetails: (Long) -> Unit,
    onBack: () -> Unit
) {
    val movies by viewModel.movies.collectAsState()
    var movieToDelete by remember { mutableStateOf<MovieEntry?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(listName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToSearch(listId) }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Filme")
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies) { movie ->
                Card(
                    modifier = Modifier.combinedClickable(
                        onClick = { onNavigateToDetails(movie.id) },
                        onLongClick = { movieToDelete = movie }
                    )
                ) {
                    AsyncImage(
                        model = movie.posterUrl,
                        contentDescription = movie.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 3f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

    // Diálogo para DELETAR filme
    movieToDelete?.let { movie ->
        AlertDialog(
            onDismissRequest = { movieToDelete = null },
            title = { Text("Remover Filme") },
            text = { Text("Deseja remover '${movie.title}' desta lista?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteMovie(movie)
                    movieToDelete = null
                }) { Text("Remover") }
            },
            dismissButton = {
                TextButton(onClick = { movieToDelete = null }) { Text("Cancelar") }
            }
        )
    }
}