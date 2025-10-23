package com.example.cinelist.ui.screen.moviedetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel(),
    movieId: Long,
    onBack: () -> Unit
) {
    val movie by viewModel.movie.collectAsState()
    val notes = viewModel.notes
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movie?.title ?: "Carregando...") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                },
                actions = {
                    // Botão SHARE
                    IconButton(onClick = { viewModel.shareMovie(context) }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartilhar")
                    }
                    // Botão UPDATE (Salvar)
                    IconButton(onClick = viewModel::saveNotes) {
                        Icon(Icons.Default.Save, contentDescription = "Salvar Notas")
                    }
                }
            )
        }
    ) { padding ->
        movie?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = it.posterUrl,
                    contentDescription = it.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Fit
                )
                Text(it.title, style = MaterialTheme.typography.headlineMedium)
                Text("Ano: ${it.year}", style = MaterialTheme.typography.titleMedium)

                // Campo UPDATE
                OutlinedTextField(
                    value = notes,
                    onValueChange = viewModel::onNotesChange,
                    label = { Text("Minhas Notas") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp)
                )
            }
        } ?: Box(modifier = Modifier.fillMaxSize()) {
            // TODO: Adicionar um CircularProgressIndicator
        }
    }
}