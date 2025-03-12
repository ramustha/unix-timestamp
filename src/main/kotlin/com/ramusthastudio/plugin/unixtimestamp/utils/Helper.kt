package com.ramusthastudio.plugin.unixtimestamp.utils

import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.InlineInlayPosition
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
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
            val secondsPart = timestamp.substring(0, dotIndex).toLong()
            val nanosPart = timestamp.substring(dotIndex + 1).padEnd(DEFAULT_DECIMAL_LENGTH, '0').toLong()
            Instant.ofEpochSecond(secondsPart, nanosPart)
        } else {
            val cleanedTimestamp = timestamp.dropLastChar()
            val longValue = cleanedTimestamp.toLong()
            when (cleanedTimestamp.length) {
                NANOS_LENGTH -> Instant.ofEpochMilli(longValue / 1_000_000)
                MICROS_LENGTH -> Instant.ofEpochMilli(longValue / 1_000)
                MILLIS_LENGTH -> Instant.ofEpochMilli(longValue)
                else -> Instant.ofEpochSecond(longValue)
            }
        }
    }

    fun createTimestamp(value: String, formatter: DateTimeFormatter): Long {
        val localDateTime = LocalDateTime.parse(value, formatter)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun findUnixTimestamp(
        text: String,
        isSupportMicroSeconds: Boolean = true,
        isSupportNanoSeconds: Boolean = true
    ): Sequence<String> {
        return TIMESTAMP_REGEX.findAll(text)
            .map { it.value }
            .filter { value ->
                val hasDecimalOrSuffix = value.contains(".") || value.endsWith('l', true)
                if (hasDecimalOrSuffix) return@filter true

                when (value.dropLastChar().length) {
                    SECONDS_LENGTH -> true
                    MILLIS_LENGTH -> true
                    MICROS_LENGTH -> isSupportMicroSeconds
                    NANOS_LENGTH -> isSupportNanoSeconds
                    else -> false
                }
            }
            .distinct()
    }

    fun findTextRanges(sentence: String, wordToFind: String): Sequence<TextRange> {
        val pattern = """\b$wordToFind(\.\d{1,$DEFAULT_DECIMAL_LENGTH})?\b""".toRegex()
        return pattern.findAll(sentence)
            .map { TextRange(it.range.first, it.range.last + 1) }
    }

    private fun String.dropLastChar(): String =
        if (isNotEmpty() && last().equals('l', ignoreCase = true)) dropLast(1) else this

    fun createInlayHintsElement(
        element: PsiElement,
        sink: InlayTreeSink,
        appSettingsState: AppSettingsState
    ) {
        val text = element.text
        val formatter = appSettingsState.defaultLocalFormatter
        val placeEndOfLine = appSettingsState.isInlayHintsPlaceEndOfLineEnable
        val uniqueIndices = mutableSetOf<Int>()

        findUnixTimestamp(
            text,
            appSettingsState.isSupportMicroSecondsEnable,
            appSettingsState.isSupportNanoSecondsEnable
        )
            .flatMap { word ->
                findTextRanges(text, word)
                    .map { word to it }
            }
            .forEach { (word, textRange) ->
                val offset = if (placeEndOfLine) textRange.endOffset else textRange.startOffset
                if (uniqueIndices.add(offset)) {
                    val instant = createInstantFormat(word)
                    val hint = formatter.format(instant)

                    sink.addPresentation(InlineInlayPosition(offset, false), hasBackground = true) {
                        text(hint)
                    }
                }
            }

        uniqueIndices.clear()
    }
}