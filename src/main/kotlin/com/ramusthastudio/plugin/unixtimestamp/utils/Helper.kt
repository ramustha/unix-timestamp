package com.ramusthastudio.plugin.unixtimestamp.utils

import com.intellij.openapi.util.TextRange
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Helper {
    private const val SECONDS_LENGTH = 10
    private const val MILLIS_LENGTH = 13
    private const val MICROS_LENGTH = 16
    private const val NANOS_LENGTH = 19
    private const val DEFAULT_DECIMAL_LENGTH = 9

    private val TIMESTAMP_REGEX = Regex(
        """\b(\d{10,13}[lL]?|\d{16,19})(\.\d{1,$DEFAULT_DECIMAL_LENGTH})?\b"""
    )

    fun createInstantFormat(timestamp: String): Instant {
        val dotIndex = timestamp.indexOf('.')
        return if (dotIndex != -1) {
            createInstantFormat(timestamp.substring(0, dotIndex))
        } else {
            val cleanedTimestamp = timestamp.dropLastChar()
            val longValue = cleanedTimestamp.toLong()
            when (cleanedTimestamp.length) {
                NANOS_LENGTH -> Instant.ofEpochMilli(longValue / 1_000_000)
                MICROS_LENGTH -> Instant.ofEpochMilli(longValue / 1_000)
                MILLIS_LENGTH -> Instant.ofEpochMilli(longValue)
                SECONDS_LENGTH -> Instant.ofEpochSecond(longValue)
                else -> Instant.ofEpochMilli(longValue / 1_000_000)
            }
        }
    }

    fun createTimestamp(value: String, formatter: DateTimeFormatter): Long {
        val localDateTime = LocalDateTime.parse(value, formatter)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun findUnixTimestamp(
        text: String
    ): Sequence<Pair<String, TextRange>> {
        return TIMESTAMP_REGEX.findAll(text)
            .map { it.value to TextRange(it.range.first, it.range.last + 1) }
    }

    private fun String.dropLastChar(): String =
        if (isNotEmpty() && last().equals('l', ignoreCase = true)) dropLast(1) else this

}
