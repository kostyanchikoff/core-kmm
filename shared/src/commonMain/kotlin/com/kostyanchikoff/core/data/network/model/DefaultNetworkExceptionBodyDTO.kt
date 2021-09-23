package com.kostyanchikoff.core.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * DTO для базовых ошибок с сервера
 */
@Serializable
open class DefaultNetworkExceptionBodyDTO(
    @SerialName("message") val message: String,
    @SerialName("documentation_url") val documentationUrl: String
)