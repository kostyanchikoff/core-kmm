package com.kostyanchikoff.core.android.githubUsers.widgets

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kostyanchikoff.core.android.config.Navigation
import com.kostyanchikoff.core.android.githubUsers.screen.GitHubUserScreen

/**
 * Виджет для работы с навигацией
 */
@Composable
fun AppNavController(){
    val navHostController = rememberNavController()
  return  NavHost(navController = navHostController, startDestination = Navigation.Route.gitHubUsersScreen) {
        composable(Navigation.Route.gitHubUsersScreen) {
            GitHubUserScreen(navHostController)
        }
    }
}