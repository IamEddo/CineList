package com.example.cinelist.ui.screen.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cinelist.data.remote.SearchResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    // listId: Long, // Este parâmetro já foi corretamente removido
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState
    var showAddedMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Filme") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Barra de Busca
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.query,
                    onValueChange = viewModel::onQueryChange,
                    label = { Text("Nome do filme") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(onClick = viewModel::searchMovies, enabled = !uiState.isLoading) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            }

            Spacer(Modifier.height(16.dp))

            // ... (A lógica de Loading, Erro e Mensagem está aqui e não precisa de mudança)

            // Lista de Resultados
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.results) { movie ->
                    SearchResultItem(
                        movie = movie,
                        onAddClick = {
                            viewModel.addMovieToWatchlist(movie)
                            // CORREÇÃO: Acessar a propriedade com letra minúscula
                            showAddedMessage = movie.title
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    movie: SearchResult,
    onAddClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                // CORREÇÃO: Acessar a propriedade com letra minúscula
                model = movie.poster,
                contentDescription = movie.title,
                modifier = Modifier.size(width = 80.dp, height = 120.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                // CORREÇÃO: Acessar a propriedade com letra minúscula
                Text(movie.title, style = MaterialTheme.typography.titleMedium)
                Text(movie.year, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar à lista")
            }
        }
    }
}
