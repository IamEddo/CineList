package com.example.cinelist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cinelist.ui.screen.list.WatchlistListScreen
import com.example.cinelist.ui.screen.moviedetails.MovieDetailsScreen
import com.example.cinelist.ui.screen.movielist.MovieListScreen
import com.example.cinelist.ui.screen.search.SearchScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "watchlists") {
        composable("watchlists") {
            WatchlistListScreen(
                onNavigateToMovieList = { listId, listName ->
                    // A navegação continua passando o listName na URL, pois o ViewModel precisa dele
                    navController.navigate("movielist/$listId/$listName")
                }
            )
        }
        composable(
            route = "movielist/{listId}/{listName}",
            arguments = listOf(
                navArgument("listId") { type = NavType.LongType },
                navArgument("listName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: 0
            MovieListScreen(
                listId = listId,
                onNavigateToSearch = { currentListId -> navController.navigate("search/$currentListId") },
                onNavigateToDetails = { movieId -> navController.navigate("details/$movieId") },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "search/{listId}",
            arguments = listOf(navArgument("listId") { type = NavType.LongType })
        ) { _ -> // CORREÇÃO: 'backStackEntry' renomeado para '_'
            SearchScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "details/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.LongType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getLong("movieId") ?: 0
            MovieDetailsScreen(
                movieId = movieId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
