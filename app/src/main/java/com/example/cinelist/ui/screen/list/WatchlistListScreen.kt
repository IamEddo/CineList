package com.example.cinelist.ui.screen.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cinelist.data.local.Watchlist
import com.example.cinelist.ui.screen.list.WatchlistListViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WatchlistListScreen(
    viewModel: WatchlistListViewModel = hiltViewModel(),
    onNavigateToMovieList: (Long, String) -> Unit
) {
    val watchlists by viewModel.watchlists.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<Watchlist?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CineList - Minhas Listas") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Lista")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(watchlists) { list ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { onNavigateToMovieList(list.id, list.name) },
                            onLongClick = { showDeleteDialog = list }
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = list.name, style = MaterialTheme.typography.titleLarge)
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Deletar (Pressione e segure)",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }

    // Diálogo para CRIAR lista
    if (showAddDialog) {
        AddWatchlistDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name ->
                viewModel.createWatchlist(name)
                showAddDialog = false
            }
        )
    }

    // Diálogo para DELETAR lista
    showDeleteDialog?.let { list ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Deletar Lista") },
            text = { Text("Tem certeza que deseja deletar a lista '${list.name}'? Todos os filmes contidos nela serão perdidos.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteWatchlist(list)
                    showDeleteDialog = null
                }) { Text("Deletar") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun AddWatchlistDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Lista") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Nome da Lista") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }, enabled = text.isNotBlank()) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}