package com.ramusthastudio.plugin.unixtimestamp.utils

import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.InlineInlayPosition
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.ramusthastudio.plugin.unixtimestamp.hints.FixedSizeSet
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Helper {
    private const val SECONDS_LENGTH = 10
    private const val MILLIS_LENGTH = 13
    private const val MICROS_LENGTH = MILLIS_LENGTH + 3
    private const val NANOS_LENGTH = MICROS_LENGTH + 3
    private const val DEFAULT_DECIMAL_LENGTH = 9

    private val TIMESTAMP_REGEX = Regex(
        "\\b(\\d{$SECONDS_LENGTH,$MILLIS_LENGTH}([lL])?|\\d{$MICROS_LENGTH,$NANOS_LENGTH})(\\.\\d{1,$DEFAULT_DECIMAL_LENGTH})?\\b"
    )

    fun createInstantFormat(timestamp: String): Instant {
        val dotIndex = timestamp.indexOf('.')
        return if (dotIndex != -1) {
            val secondsPart = timestamp.substring(0, dotIndex).toLong()
            val nanosPart = timestamp.substring(dotIndex + 1).padEnd(DEFAULT_DECIMAL_LENGTH, '0').toLong()
            Instant.ofEpochSecond(secondsPart, nanosPart)
        } else {
            when (timestamp.length) {
                NANOS_LENGTH -> Instant.ofEpochMilli(timestamp.toLong() / 1_000_000)
                MICROS_LENGTH -> Instant.ofEpochMilli(timestamp.toLong() / 1_000)
                MILLIS_LENGTH -> Instant.ofEpochMilli(timestamp.toLong())
                else -> Instant.ofEpochSecond(timestamp.toLong())
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
    ): Set<String> {
        val results = FixedSizeSet<String>(1000)
        TIMESTAMP_REGEX.findAll(text).forEach { match ->
            val value = match.value
            if (
                value.length == SECONDS_LENGTH ||
                value.length == MILLIS_LENGTH ||
                (value.length == MICROS_LENGTH && isSupportMicroSeconds) ||
                (value.length == NANOS_LENGTH && isSupportNanoSeconds) ||
                value.contains(".") ||
                value.last().equals('l', ignoreCase = true)
            ) {
                results.add(value)
            }
        }
        return results
    }

    fun findTextRanges(sentence: String, wordToFind: String): Sequence<TextRange> {
        val regex = Regex("\\b$wordToFind(\\.\\d{1,$DEFAULT_DECIMAL_LENGTH})?\\b")
        return regex.findAll(sentence).map { TextRange(it.range.first, it.range.last + 1) }
    }

    private fun dropLastChar(value: String): String =
        if (value.last().equals('l', ignoreCase = true)) value.dropLast(1) else value

    fun createInlayHintsElement(
        uniqueIndices: MutableSet<Int>,
        element: PsiElement,
        sink: InlayTreeSink,
        appSettingsState: AppSettingsState
    ) {
        val text = element.text
        val formatter = appSettingsState.defaultLocalFormatter
        val inlayHintsPlaceEndOfLineEnabled = appSettingsState.isInlayHintsPlaceEndOfLineEnable

        findUnixTimestamp(
            text,
            appSettingsState.isSupportMicroSecondsEnable,
            appSettingsState.isSupportNanoSecondsEnable
        )
            .flatMap { word ->
                findTextRanges(text, word).map { textRange -> word to textRange }
            }
            .parallelStream()
            .forEach { (word, textRange) ->
                val offset = if (inlayHintsPlaceEndOfLineEnabled) textRange.endOffset else textRange.startOffset
                if (uniqueIndices.add(offset)) {
                    val instant = createInstantFormat(dropLastChar(word))
                    val hint = formatter.format(instant)

                    sink.addPresentation(InlineInlayPosition(offset, false), hasBackground = true) {
                        text(hint)
                    }
                }
            }

        uniqueIndices.clear()
    }
}


