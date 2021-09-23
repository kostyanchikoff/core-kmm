package com.kostyanchikoff.core.domain.example.gitHubUsers.usecases

import com.kostyanchikoff.core.data.example.gitHubUsers.repository.GitHubUsersRepository
import com.kostyanchikoff.core.domain.example.gitHubUsers.entityes.GitHubUser
import com.kostyanchikoff.core.domain.usecases.CoreSuspendNonParamUseCase

class GetUsersUseCase(private val repo: GitHubUsersRepository) :
    CoreSuspendNonParamUseCase<GetUsersResult> {

    override suspend fun execute(): GetUsersResult {
        val result = repo.getUsers()
        return GetUsersResult(users = result)
    }
}

data class GetUsersResult(val users: List<GitHubUser>)