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
            val listName = backStackEntry.arguments?.getString("listName") ?: ""
            MovieListScreen(
                listId = listId,
                listName = listName,
                onNavigateToSearch = { navController.navigate("search/$listId") },
                onNavigateToDetails = { movieId -> navController.navigate("details/$movieId") },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "search/{listId}",
            arguments = listOf(navArgument("listId") { type = NavType.LongType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: 0
            SearchScreen(
                listId = listId,
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