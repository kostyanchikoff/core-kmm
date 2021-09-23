package com.kostyanchikoff.core.config

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Конфигуряция для http запросов
 */
val httpConfig = HttpClient() {

    defaultRequest {
        host = "api.github.com"
        url {
            protocol = URLProtocol.HTTPS
        }
    }
    install(JsonFeature) {
        serializer = KotlinxSerializer(json = kotlinx.serialization.json.Json {
            isLenient = false
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
            useArrayPolymorphism = false
        })
    }
}