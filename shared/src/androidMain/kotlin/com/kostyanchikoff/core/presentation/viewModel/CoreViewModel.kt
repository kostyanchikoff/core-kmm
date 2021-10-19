package com.kostyanchikoff.core.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.kostyanchikoff.core.data.network.exception.CoreHttpException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.net.HttpURLConnection

/**
 * Базовая ViewModel работает по принцепу MVI
 */
actual open class CoreViewModel actual constructor(
    initState: CoreViewState
) : ViewModel() {


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


    init {
        subscribeToEvents()
    }

    /**
     * функция отрабатывает в случае очистки viewModel-и
     */
    public actual override fun onCleared() = parentJob.cancel()


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
     * Функция для работы с coroutine (запускает и обрабатывает ошибки)
     * [call] - вызов suspend функции
     * [onLoading] - при загрузке
     * [onResult] - получение результа
     *  [onError] - получение ошибки
     *  [isShowPrimaryError] - если передать true тогда пользователь увидит экран с ошибком в виде UI,
     *  в противном случае будет показан тост
     */
    actual open fun <T> launch(
        call: suspend () -> T,
        isShowPrimaryError: Boolean,
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
                handleDefaultError(ex,isShowPrimaryError)
            }
        }
    }

    /**
     * запускает coroutine с возможностью обработки пользовательской ошибки
     * [call] - вызов suspend функции
     * [onLoading] - при загрузке
     * [onResult] - получение результа
     * [onError] - получение ошибки
     *  [isShowPrimaryError] - если передать true тогда пользователь увидит экран с ошибком в виде UI,
     *  в противном случае будет показан тост
     */
    actual fun <T, V> launchWithError(
        call: suspend () -> T,
        isShowPrimaryError: Boolean,
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
                    handleDefaultError(ex,isShowPrimaryError)
                    return@launch
                }

                error.let(onError)
            }
        }

    }


    /**
     * Отслеживаем ошибки
     * [ex] - исключение
     */
    private fun handleDefaultError(ex: Throwable, isShowPrimaryError: Boolean) {
        when (ex) {
            is CoreHttpException -> {
                when {
                    ex.code == HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        setEffect {
                            CoreContract.CoreErrorEffect.RedirectToLogin
                        }

                    }
                    ex.code == HttpURLConnection.HTTP_NOT_FOUND -> {
                        showError(
                            errorState = CoreContract.CoreErrorState.NotFountPage,
                            errorMessage = ex.error,
                            isShowPrimaryError = isShowPrimaryError
                        )

                    }
                    ex.code == HttpURLConnection.HTTP_INTERNAL_ERROR -> {

                        showError(
                            errorState = CoreContract.CoreErrorState.ServerInternalError,
                            errorMessage = ex.error,
                            isShowPrimaryError = isShowPrimaryError
                        )


                    }

                    ex.error.isNotEmpty() -> {
                        showError(
                            errorState = CoreContract.CoreErrorState.ShowErrorMessage(
                                ex.message.orEmpty()
                            ), errorMessage = ex.error,
                            isShowPrimaryError = isShowPrimaryError
                        )


                    }
                }
            }
            else -> {
                showError(
                    errorState = CoreContract.CoreErrorState.ShowErrorMessage(
                        ex.message.orEmpty()
                    ), errorMessage = ex.message,
                    isShowPrimaryError = isShowPrimaryError
                )

            }
        }
    }

    /**
     * Показ ошибки на UI
     */
    private fun showError(
        errorState: CoreViewState,
        errorMessage: String?,
        isShowPrimaryError: Boolean
    ) {
        if (isShowPrimaryError) {
            setState {
                errorState
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