package com.kostyanchikoff.core.domain.example.gitHubUsers.entityes

import com.kostyanchikoff.core.data.example.gitHubUsers.model.GitHubUsersDTO


data class GitHubUser(
    val avatarUrl: String,
    val gravatarId: String,
    val login: String,
)


fun  List<GitHubUsersDTO>.fromDTO() : List<GitHubUser>{
    val list = mutableListOf<GitHubUser>()

    forEach {
        val value = GitHubUser(
            avatarUrl = it.avatar_url,
            gravatarId = it.gravatar_id,
            login = it.login
        )
        list.add(value)
    }

    return  list
}
