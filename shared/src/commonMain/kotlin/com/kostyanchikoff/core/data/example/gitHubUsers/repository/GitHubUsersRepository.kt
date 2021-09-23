package com.kostyanchikoff.core.data.example.gitHubUsers.repository

import com.kostyanchikoff.core.data.example.gitHubUsers.api.GitHubUsersApiService
import com.kostyanchikoff.core.domain.example.gitHubUsers.entityes.fromDTO
import com.kostyanchikoff.core.utils.network.safeApiCall
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class GitHubUsersRepository(private val apiService: GitHubUsersApiService) {

    suspend fun getUsers() = safeApiCall {
        apiService.getUsers()
    }.fromDTO()


    suspend fun getUsersById(userId: Int) = safeApiCall {  apiService.getUserById(userId) }

//    suspend fun getUsersById(userId: Int) =
//        safeApiCallWithError<TestUsersDTO, GitHubUserErrorBodyDTO>({
//            apiService.getUserById(userId)
//        }) {
//
//            print(it)
//            throw  Exception()
//        }
}

@Serializable
data class GitHubUserErrorBodyDTO(
    @SerialName("message") val message: String,
    @SerialName("documentation_url") val documentationUrl: String
)

