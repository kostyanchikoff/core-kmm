package com.kostyanchikoff.core.utils.network

import com.kostyanchikoff.core.data.example.gitHubUsers.repository.GitHubUserErrorBodyDTO
import com.kostyanchikoff.core.data.network.exception.CoreHttpException
import com.kostyanchikoff.core.data.network.model.DefaultNetworkExceptionBodyDTO
import io.ktor.client.features.*
import io.ktor.http.*
import io.ktor.utils.io.*



/**
 * Функция обрабатывает пользовательские http ошибки
 * @param call suspend функция
 * @param errorBloc - лямбда принимает пользовательскую ошибку
 */
suspend inline fun <T : Any, reified V : Any> safeApiCallWithError(
    noinline call: suspend () -> T,
    noinline errorBloc: (GitHubUserErrorBodyDTO?) -> Throwable
): T {
    return try {
        call.invoke()
    } catch (e: Throwable) {
        throw  handleHttpException(e, errorBloc)
    }
}


/**
 * Функция обрабатывает  http ошибки
 * @param call suspend функция
 */
suspend fun <T : Any> safeApiCall(
    call: suspend () -> T
): T {
    return try {
        call.invoke()
    } catch (e: Throwable) {
        throw  handleHttpException<DefaultNetworkExceptionBodyDTO>(e)
    }
}

/**
 * Слушатель ошибок при http запросах
 * [ex] - исключение
 * [errorBloc] - лямбда для перехвата ошибок
 */
suspend inline fun <reified V : Any> handleHttpException(
    ex: Throwable,
    noinline errorBloc: ((V?) -> Throwable)? = null,
): CoreHttpException {
    return when (ex) {
        is ResponseException -> handleResponseException(ex, errorBloc)
        is CancellationException -> throw CoreHttpException(
            error = "Сервер не отвечает",
        )
        else -> throw Throwable(message = ex.message)
    }
}

/**
 * Слушатель ошибок при http запросах которые возникают при code > 200
 * [ex] - исключение
 * [errorBloc] - лямбда для перехвата ошибок
 */
suspend inline fun <reified V> handleResponseException(
    ex: ResponseException,
    noinline errorBloc: ((V?) -> Throwable)?
): CoreHttpException {
    val statusCode = ex.response.status.value
    val errorBodyUTF8 = ex.response.content.readUTF8Line()

    if (statusCode == HttpStatusCode.NotFound.value) {
        throw CoreHttpException(error = "Данного запроса не существует", code = statusCode)
    }

    if (errorBloc == null) {
        val errorBody = parseJson<DefaultNetworkExceptionBodyDTO>(errorBodyUTF8)
        throw  CoreHttpException(error = errorBody?.message.orEmpty(), code = statusCode)
    } else {
        val errorBody = parseJson<V>(errorBodyUTF8)
        throw errorBloc.invoke(errorBody)
    }
}



