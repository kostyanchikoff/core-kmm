package com.kostyanchikoff.core.di

import com.kostyanchikoff.core.config.httpConfig
import com.kostyanchikoff.core.data.example.gitHubUsers.api.GitHubUsersApiService
import com.kostyanchikoff.core.data.example.gitHubUsers.repository.GitHubUsersRepository
import com.kostyanchikoff.core.domain.example.gitHubUsers.usecases.GetUsersUseCase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

val coreCommonModules = DI.Module {
    bind<GitHubUsersApiService>() with factory { GitHubUsersApiService(httpConfig) }
    bind<GitHubUsersRepository>() with factory { GitHubUsersRepository(instance()) }
    bind<GetUsersUseCase>() with factory { GetUsersUseCase(instance()) }
}

val coreRepositoryModule = DI.Module {

}

val coreUseCaseModule = DI.Module {

}

val coreViewModelModule = DI.Module {

}

val di = DI {
    import(coreCommonModules)
    import(coreRepositoryModule)
    import(coreUseCaseModule)
    import(coreViewModelModule)
}