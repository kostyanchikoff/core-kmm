package com.kostyanchikoff.core.data.network.exception

import com.kostyanchikoff.core.data.constants.CoreConstant

/**
 * Пользовательское исключения для работы ошибками сервера
 */
open class CoreHttpException(
     val error: String = CoreConstant.EMPTY,
     val code: Int = CoreConstant.ZERO
) : Throwable()