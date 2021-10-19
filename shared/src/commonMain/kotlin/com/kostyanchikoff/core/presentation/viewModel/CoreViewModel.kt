package com.kostyanchikoff.core.presentation.viewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CoreViewState

interface CoreViewEvent

interface CoreViewSideEffect

/**
 * Базовый класс viewModel для всех платформ
 */
@Suppress("EmptyDefaultConstructor")
expect open class CoreViewModel(initState: CoreViewState) {


    /**
     * StateFlow для работы с стоянияними (работа с UI)
     */
    val viewState: StateFlow<CoreViewState>

    /**
     * SharedFlow для отправки события в ViewModel
     */
    val event: SharedFlow<CoreViewEvent>

    /**
     * SharedFlow событие для работы с MVI (эффект связан с работой навигации, snackBar и т.д)
     */
    val effect: SharedFlow<CoreViewSideEffect>


    /**
     * Job для работы с карутинами
     */
    protected val parentJob: Job


    /**
     * Область работы сопрограммы
     */
    protected val scope: CoroutineScope


    /**
     * Функция для работы с coroutine (запускает и обрабатывает ошибки)
     * [call] - вызов suspend функции
     * [onLoading] - при загрузке
     * [onResult] - получение результа
     *  [onError] - получение ошибки
     *  [isShowPrimaryError] - если передать true тогда пользователь увидит экран с ошибком в виде UI,
     *  в противном случае будет показан тост
     */
    open fun <T> launch(
        call: suspend () -> T,
        isShowPrimaryError : Boolean = false,
        onLoading: ((Boolean) -> Unit)? = null,
        onResult: (T) -> Unit,
        onError: ((String) -> Unit?)? = null
    ): Job


    /**
     * запускает coroutine с возможностью обработки пользовательской ошибки
     * [call] - вызов suspend функции
     * [onLoading] - при загрузке
     * [onResult] - получение результа
     * [onError] - получение ошибки
     * [isShowPrimaryError] - если передать true тогда пользователь увидит экран с ошибком в виде UI,
     *  в противном случае будет показан тост
     */
    fun <T, V> launchWithError(
        call: suspend () -> T,
        isShowPrimaryError : Boolean = false,
        onLoading: ((Boolean) -> Unit)? = null,
        onResult: (T) -> Unit,
        onError: (V) -> Unit?
    ): Job

    /**
     * функция отрабатывает в случае очистки viewModel-и
     */
    open fun onCleared()

    /**
     * Случатель получения эффекта
     * [event] - событие
     */
    open fun handleEvents(event: CoreViewEvent)

    /**
     * Отправка состояния
     * [reducer] - лямбда приемник для отправки состояния
     */
    protected fun setState(reducer: CoreViewState.() -> CoreViewState)

    /**
     * Отправка события
     * [event] - собятие
     */
    protected fun setEvent(event: CoreViewEvent)

    /**
     * отправка эффекта
     * [builder] - лямбда приемник для отправки эффекта
     */
    protected fun setEffect(builder: () -> CoreViewSideEffect)


}
