package com.kostyanchikoff.core.data.constants

import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object CoreVariables {

    /**
     * url для REST API
     */
    var BASE_URL = CoreConstant.EMPTY

    /**
     * url для изображений
     */
    var IMAGE_URL = CoreConstant.EMPTY

    /**
     * Для хранение url-ов которые не нуждаються в token
     */
    var URLS_OF_UNNECESSARY_BEARER_TOKEN_ENDPOINTS: List<String> = emptyList()

    /**
     * Базовый header для refresh token
     */
    var BASIC_REFRESH_AUTH_HEADER = CoreConstant.EMPTY

    /**
     * Запрос для refresh token
     */
    var REFRESH_TOKEN_END_POINT = CoreConstant.EMPTY

    /**
     * Флаг для проверки продовой версии приложения
     */
    var IS_PRODUCTION = true
}