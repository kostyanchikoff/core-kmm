package com.kostyanchikoff.core.android.githubUsers.screen

import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kostyanchikoff.core.android.core.widgets.CoreViewModelConsumer
import com.kostyanchikoff.core.android.githubUsers.widgets.GitHubUserItemWidget
import com.kostyanchikoff.core.presentation.example.gitHubUsers.viewModels.GitHubUserEvent
import com.kostyanchikoff.core.presentation.example.gitHubUsers.viewModels.GitHubUserState
import com.kostyanchikoff.core.presentation.example.gitHubUsers.viewModels.GitHubUsersViewModel

/**
 * Экран пользователей github
 * [navController] - контроллер для работы с нивигацией
 */
@Composable
fun GitHubUserScreen(navController: NavController) {
    val viewModel = viewModel<GitHubUsersViewModel>()
    viewModel.setEvent(GitHubUserEvent.GetUsers)

    CoreViewModelConsumer(
        scaffoldState = rememberScaffoldState(),
        viewModel = viewModel,
        stateBuilder = {
            when (it) {
                is GitHubUserState.LoadingState -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is GitHubUserState.LoadedState -> {
                    LazyColumn {
                        items(
                            count = it.users.count(),
                            itemContent = { index ->
                                GitHubUserItemWidget(
                                    avatarUrl = it.users[index].avatarUrl,
                                )
                            }
                        )

                    }
                }
            }
        },
    )
}
