package com.kostyanchikoff.core.domain.usecases

/**
 * Интерфейс для реализации useCase без входящего параметра
 * @param T параметр метода
 * @param V результат выполненного действия
 */
interface CoreNoneParamUseCase<out V : Any> {

    fun execute(): V
}


/**
 * Интерфейс для реализации useCase с входящем параметром
 * @param T параметр метода
 * @param V результат выполненного действия
 */
interface CoreUseCase<out V : Any> {

    fun execute(): V
}


/**
 * Интерфейс для реализации useCase с входящем параметром c suspend функцией
 * @param T параметр метода
 * @param V результат выполненного действия
 */
interface CoreSuspendUseCase<in I, out V : Any> {

    suspend fun execute(param: I): V
}

/**
 * Интерфейс для реализации useCase  без входящего параметром c suspend функцией
 * @param V результат выполненного действия
 */
interface CoreSuspendNonParamUseCase<out V : Any> {

    suspend fun execute(): V
}

