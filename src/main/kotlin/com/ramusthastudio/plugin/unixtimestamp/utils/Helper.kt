package com.ramusthastudio.plugin.unixtimestamp.utils

import com.intellij.openapi.util.TextRange
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap

object Helper {
    private const val SECONDS_LENGTH = 10
    private const val MILLIS_LENGTH = 13
    private const val MICROS_LENGTH = 16
    private const val NANOS_LENGTH = 19
    private const val DEFAULT_DECIMAL_LENGTH = 9

    private val TIMESTAMP_REGEX = Regex(
        """\b(\d{10,13}[lL]?|\d{16,19})(\.\d{1,$DEFAULT_DECIMAL_LENGTH})?\b"""
    )
    
    private val instantCache = ConcurrentHashMap<String, Instant>(100)
    
    fun createInstantFormat(timestamp: String): Instant {
        return instantCache.getOrPut(timestamp) {
            val dotIndex = timestamp.indexOf('.')
            if (dotIndex != -1) {
                createInstantFormat(timestamp.substring(0, dotIndex))
            } else {
                convertTimestampToInstant(timestamp)
            }
        }
    }
    
    private fun convertTimestampToInstant(timestamp: String): Instant {
        if (timestamp.isEmpty()) return Instant.EPOCH
        
        val cleanedTimestamp = if (timestamp.isNotEmpty() && (timestamp.last() == 'l' || timestamp.last() == 'L')) {
            timestamp.substring(0, timestamp.length - 1)
        } else {
            timestamp
        }
        
        try {
            val longValue = cleanedTimestamp.toLong()
            return when (cleanedTimestamp.length) {
                NANOS_LENGTH -> Instant.ofEpochMilli(longValue / 1_000_000)
                MICROS_LENGTH -> Instant.ofEpochMilli(longValue / 1_000)
                MILLIS_LENGTH -> Instant.ofEpochMilli(longValue)
                SECONDS_LENGTH -> Instant.ofEpochSecond(longValue)
                else -> Instant.ofEpochMilli(longValue / 1_000_000)
            }
        } catch (e: NumberFormatException) {
            return Instant.EPOCH
        }
    }

    fun createTimestamp(value: String, formatter: DateTimeFormatter): Long {
        val localDateTime = LocalDateTime.parse(value, formatter)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun findUnixTimestamp(text: String): Sequence<Pair<String, TextRange>> {
        if (text.length < SECONDS_LENGTH) return emptySequence()
        
        return if (text.length > 10000) {
            TIMESTAMP_REGEX.findAll(text)
                .asIterable()
                .asSequence()
                .map { it.value to TextRange(it.range.first, it.range.last + 1) }
        } else {
            TIMESTAMP_REGEX.findAll(text)
                .map { it.value to TextRange(it.range.first, it.range.last + 1) }
        }
    }
}
