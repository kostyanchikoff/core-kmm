package com.kostyanchikoff.core.presentation.viewModel

/**
 * Контракт для работы с стейтами, эффектами
 */
class CoreContract {

    /**
     * Базовое состояние
     */
    sealed class CoreErrorState : CoreViewState {
        /**
         * Состояние когда запрос не найден
         */
        object NotFountPage : CoreErrorState()

        /**
         * Состояние когда ошибка на сервере
         */
        object ServerInternalError : CoreErrorState()

        /**
         * Состояние когда происходит первичная неизветная ошибка
         */
        data class ShowErrorMessage(val message: String) : CoreErrorState()
    }

    /**
     * Базовый эффект
     */
    sealed class CoreErrorEffect : CoreViewSideEffect {
        /**
         * Эффект когда происходит когда пользователь ловит 401 ошибку
         */
        object RedirectToLogin : CoreErrorEffect()

        /**
         * Эффект когда происходит обстрактая ошибка
         */
        data class ShowErrorMessage(val errorMessage: String) : CoreErrorEffect()
    }


    /**
     * Базовое состояние при первичной загрузке
     */
    open class CoreLoadingState : CoreViewState



}