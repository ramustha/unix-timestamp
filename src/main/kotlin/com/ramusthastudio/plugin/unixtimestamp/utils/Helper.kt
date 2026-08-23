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

    fun createInstantFormat(timestamp: String): Instant? {
        return convertTimestampToInstant(timestamp.substringBefore('.'))
    }

    private fun convertTimestampToInstant(timestamp: String): Instant? {
        val cleanedTimestamp = timestamp.removeLongSuffix()
        val longValue = cleanedTimestamp.toLongOrNull() ?: return null
        return when (cleanedTimestamp.length) {
            NANOS_LENGTH -> Instant.ofEpochMilli(longValue / 1_000_000)
            MICROS_LENGTH -> Instant.ofEpochMilli(longValue / 1_000)
            MILLIS_LENGTH -> Instant.ofEpochMilli(longValue)
            SECONDS_LENGTH -> Instant.ofEpochSecond(longValue)
            else -> null
        }
    }

    fun createTimestamp(value: String, formatter: DateTimeFormatter): Long {
        val localDateTime = LocalDateTime.parse(value, formatter)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun findUnixTimestamp(
        text: CharSequence,
        isSupportMicroSeconds: Boolean = true,
        isSupportNanoSeconds: Boolean = true,
        maxMatches: Int = Int.MAX_VALUE
    ): Sequence<Pair<String, TextRange>> {
        if (text.length < SECONDS_LENGTH || maxMatches <= 0) return emptySequence()

        return TIMESTAMP_REGEX.findAll(text)
            .filter { match ->
                when (match.value.substringBefore('.').removeLongSuffix().length) {
                    SECONDS_LENGTH, MILLIS_LENGTH -> true
                    MICROS_LENGTH -> isSupportMicroSeconds
                    NANOS_LENGTH -> isSupportNanoSeconds
                    else -> false
                }
            }
            .take(maxMatches)
            .map { match ->
                match.value to TextRange(match.range.first, match.range.last + 1)
            }
    }

    private fun String.removeLongSuffix(): String {
        return if (endsWith('l', ignoreCase = true)) dropLast(1) else this
    }
}
