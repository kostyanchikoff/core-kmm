package com.kostyanchikoff.core.utils.network

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement


/**
 * Парсит json строку
 * @param json строка
 */
inline fun <reified T> parseJson(json: String?): T? = try {
   Json.decodeFromString<T>(json.orEmpty())
} catch (ex: Exception) {
    print(ex)
    null
}