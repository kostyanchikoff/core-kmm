package com.kostyanchikoff.core.data.example.gitHubUsers.api

import com.kostyanchikoff.core.data.example.gitHubUsers.model.GitHubUsersDTO
import io.ktor.client.*
import io.ktor.client.request.*

class GitHubUsersApiService(private val httpClient: HttpClient) {

    suspend fun getUsers() = httpClient.get<List<GitHubUsersDTO>>(path = "users")

    suspend fun getUserById(userId : Int) = httpClient.get<GitHubUsersDTO>(path = "userascas/$userId")
}