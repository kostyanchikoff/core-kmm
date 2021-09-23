package com.kostyanchikoff.core.presentation.example.gitHubUsers.viewModels

import com.kostyanchikoff.core.domain.example.gitHubUsers.entityes.GitHubUser
import com.kostyanchikoff.core.presentation.viewModel.CoreContract
import com.kostyanchikoff.core.presentation.viewModel.CoreViewEvent
import com.kostyanchikoff.core.presentation.viewModel.CoreViewSideEffect
import com.kostyanchikoff.core.presentation.viewModel.CoreViewState

sealed class GitHubUserEvent : CoreViewEvent {
    object GetUsers : GitHubUserEvent()
}


sealed class GitHubUserState : CoreViewState {
    object LoadingState : CoreContract.CoreLoadingState()
    data class LoadedState(val users: List<GitHubUser>) : GitHubUserState()

}

sealed class GitHubUserEffect : CoreViewSideEffect {

}
