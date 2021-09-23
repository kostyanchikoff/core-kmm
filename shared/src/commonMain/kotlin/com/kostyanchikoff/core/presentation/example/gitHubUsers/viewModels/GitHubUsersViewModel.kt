package com.kostyanchikoff.core.presentation.example.gitHubUsers.viewModels

import com.kostyanchikoff.core.di.di
import com.kostyanchikoff.core.domain.example.gitHubUsers.entityes.GitHubUser
import com.kostyanchikoff.core.domain.example.gitHubUsers.usecases.GetUsersResult
import com.kostyanchikoff.core.domain.example.gitHubUsers.usecases.GetUsersUseCase
import com.kostyanchikoff.core.presentation.viewModel.CoreViewEvent
import com.kostyanchikoff.core.presentation.viewModel.CoreViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.instance

/**
 * Пример работы с viewModel
 */
class GitHubUsersViewModel : CoreViewModel(GitHubUserState.LoadingState) {

    /**
     * Use-case для получения пользователей
     */
    private val getUserUseCase: GetUsersUseCase by di.instance()

    /**
     * Отслеживаем события
     * [event] - событие
     */
    override fun handleEvents(event: CoreViewEvent) {
        if (event is GitHubUserEvent.GetUsers) {
            getUsers()
            return
        }
    }

    /**
     * Запрос на получение пользователей
     */
    private fun getUsers() {

        launch(
            call = {
                getUserUseCase.execute()
            },
            onResult = { value ->
                setState {
                    GitHubUserState.LoadedState(users = value.users)
                }


            }
        )
    }
}