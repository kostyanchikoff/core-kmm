package com.kostyanchikoff.core.presentation.viewModel

import com.kostyanchikoff.core.data.network.exception.CoreHttpException
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.native.internal.GC

/**
 * Базовый класс viewModel для всех платформ
 */
@Suppress("EmptyDefaultConstructor")
actual open class CoreViewModel actual constructor(
    initState: CoreViewState
) {

    /**
     * Флаг информирует что ошибка была при первичной работы с корутинами
     */
    actual open var isPrimaryError: Boolean = true

    /**
     * StateFlow для работы с стоянияними (работа с UI)
     */
    private val _viewState = MutableStateFlow(initState)
    actual val viewState: StateFlow<CoreViewState>
        get() = _viewState


    /**
     * SharedFlow для отправки события в ViewModel
     */
    private val _event = MutableSharedFlow<CoreViewEvent>()
    actual val event: SharedFlow<CoreViewEvent>
        get() = _event

    /**
     * SharedFlow событие для работы с MVI (эффект связан с работой навигации, snackBar и т.д)
     */
    private val _effect = MutableSharedFlow<CoreViewSideEffect>()
    actual val effect: SharedFlow<CoreViewSideEffect>
        get() = _effect


    /**
     * Job для работы с карутинами
     */
    protected actual val parentJob: Job
        get() = SupervisorJob()

    /**
     * Область работы сопрограммы
     */
    protected actual val scope: CoroutineScope
        get() = CoroutineScope(Dispatchers.Main + parentJob)


    init {
        subscribeToEvents()
    }


    /**
     * Отправка события
     * [event] - собятие
     */
    actual fun setEvent(event: CoreViewEvent) {
        scope.launch {
            _event.emit(event)
        }
    }

    /**
     * Случатель получения эффекта
     * [event] - событие
     */
    actual open fun handleEvents(event: CoreViewEvent) {
        // do nothing?
    }

    /**
     * функция отрабатывает в случае очистки viewModel-и
     */
    actual open fun onCleared() {
        parentJob.cancel()
        isPrimaryError = true
        dispatch_async(dispatch_get_main_queue()) {
            GC.collect()
        }
    }


    /**
     * Функция для работы с coroutine (запускает и обрабатывает ошибки)
     * [call] - вызов suspend функции
     * [onLoading] - при загрузке
     * [onResult] - получение результа
     * [onError] - получение ошибки
     */
    actual open fun <T> launch(
        call: suspend () -> T,
        onLoading: ((Boolean) -> Unit)?,
        onResult: (T) -> Unit,
        onError: ((String) -> Unit?)?
    ): Job {
        onLoading?.invoke(true)
        return scope.launch {
            try {
                val result = call.invoke()
                onLoading?.invoke(false)
                result?.let(onResult)
            } catch (ex: Throwable) {
                onLoading?.invoke(false)
                handleDefaultError(ex)
            }
        }
    }

    /**
     * запускает coroutine с возможностью обработки пользовательской ошибки
     * [call] - вызов suspend функции
     * [onLoading] - при загрузке
     * [onResult] - получение результа
     * [onError] - получение ошибки
     */
    actual fun <T, V> launchWithError(
        call: suspend () -> T,
        onLoading: ((Boolean) -> Unit)?,
        onResult: (T) -> Unit,
        onError: (V) -> Unit?
    ): Job {

        return scope.launch {
            try {
                val result = call.invoke()
                onLoading?.invoke(false)
                result?.let(onResult)
            } catch (ex: Throwable) {
                onLoading?.invoke(false)
                val error = ex as? V

                if (error == null) {
                    handleDefaultError(ex)
                    return@launch
                }

                error.let(onError)
            }
        }

    }


    /**
     * отправка эффекта
     * [builder] - лямбда приемник для отправки эффекта
     */
    protected actual fun setEffect(builder: () -> CoreViewSideEffect) {
        val effectValue = builder()
        scope.launch {
            _effect.emit(effectValue)
        }
    }


    /**
     * Отправка состояния
     * [reducer] - лямбда приемник для отправки состояния
     */
    protected actual fun setState(reducer: CoreViewState.() -> CoreViewState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }


    /**
     * Отслеживаем ошибки
     * [ex] - исключение
     */
    private fun handleDefaultError(ex: Throwable) {
        when (ex) {
            is CoreHttpException -> {
                when {
                    ex.code == HttpStatusCode.Unauthorized.value -> {
                        setEffect {
                            CoreContract.CoreErrorEffect.RedirectToLogin
                        }

                    }
                    ex.code == HttpStatusCode.NotFound.value -> {
                        showError(
                            primaryErrorState = CoreContract.CoreErrorState.NotFountPage,
                            errorMessage = ex.error
                        )

                    }
                    ex.code == HttpStatusCode.InternalServerError.value -> {

                        showError(
                            primaryErrorState = CoreContract.CoreErrorState.ServerInternalError,
                            errorMessage = ex.error
                        )


                    }

                    ex.error.isNotEmpty() -> {
                        showError(
                            primaryErrorState = CoreContract.CoreErrorState.ShowErrorMessage(
                                ex.message.orEmpty()
                            ), errorMessage = ex.error
                        )


                    }
                }
            }
            else -> {
                showError(
                    primaryErrorState = CoreContract.CoreErrorState.ShowErrorMessage(
                        ex.message.orEmpty()
                    ), errorMessage = ex.message
                )

            }
        }
        isPrimaryError = false
    }

    /**
     * Показ ошибки на UI
     */
    private fun showError(primaryErrorState: CoreViewState, errorMessage: String?) {
        if (isPrimaryError) {
            setState {
                primaryErrorState
            }
            return
        }

        if (errorMessage != null) {
            setEffect {
                CoreContract.CoreErrorEffect.ShowErrorMessage(errorMessage)
            }
        }
    }

    /**
     * Подписываемся на события
     */
    private fun subscribeToEvents() {
        scope.launch {
            event.collect {
                handleEvents(it)
            }
        }
    }


}
