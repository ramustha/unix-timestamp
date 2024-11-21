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
import java.util.regex.Pattern

object Helper {
    private const val SECONDS_LENGTH = 10
    private const val MILLIS_LENGTH = 13
    private const val MICROS_LENGTH = MILLIS_LENGTH + 3
    private const val NANOS_LENGTH = MICROS_LENGTH + 3
    private const val DEFAULT_DECIMAL_LENGTH = 9

    fun createInstantFormat(timestamp: String): Instant {
        return if (timestamp.length == NANOS_LENGTH) {
            Instant.ofEpochMilli(timestamp.toLong() / 1_000_000)
        } else if (timestamp.length == MICROS_LENGTH) {
            Instant.ofEpochMilli(timestamp.toLong() / 1_000)
        } else if (timestamp.length == MILLIS_LENGTH) {
            Instant.ofEpochMilli(timestamp.toLong())
        } else if (timestamp.contains(".")) {
            val parts = timestamp.split(".")
            Instant.ofEpochSecond(
                parts[0].toLong(),
                parts[1].padEnd(DEFAULT_DECIMAL_LENGTH, '0').toLong() // pad to nano seconds to allow arbitrary precision in input
            )
        }  else {
            Instant.ofEpochSecond(timestamp.toLong())
        }
    }

    fun createTimestamp(value: String, formatter: DateTimeFormatter): Long {
        val localDateTime = LocalDateTime.parse(value, formatter)
        val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        return instant.toEpochMilli()
    }

    fun findUnixTimestamp(text: String): List<String> {
        val regex = String.format(
            "\\b(\\d{%s,%s}([lL])?|\\d{%s,%s})(\\.\\d{1,%s})?\\b",
            SECONDS_LENGTH,
            MILLIS_LENGTH,
            MICROS_LENGTH,
            NANOS_LENGTH,
            DEFAULT_DECIMAL_LENGTH
        )
        val pattern = regex.toRegex()
        return pattern.findAll(text)
            .map { it.value }
            .filter {
                it.length == SECONDS_LENGTH
                        || it.length == MILLIS_LENGTH
                        || it.length == MICROS_LENGTH
                        || it.length == NANOS_LENGTH
                        || it.endsWith("l")
                        || it.endsWith("L")
                        || it.contains(".")
            }
            .distinct()
            .toList()
    }

    private fun dropLastChar(value: String): String {
        if ((value.last() == 'l') or (value.last() == 'L')) {
            return value.dropLast(1)
        }
        return value
    }

    fun findTextRanges(sentence: String, wordToFind: String): List<TextRange> {
        val regex = String.format("\\b$wordToFind?(.\\d{1,%s})?\\b", DEFAULT_DECIMAL_LENGTH)
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(sentence)
        val indexList = mutableListOf<TextRange>()
        while (matcher.find()) {
            indexList.add(TextRange(matcher.start(), matcher.end()))
        }
        return indexList
    }

    fun createInlayHintsElement(
        uniqueIndices: MutableSet<Int>,
        element: PsiElement,
        sink: InlayTreeSink,
        appSettingsState: AppSettingsState
    ) {
        val text = element.text
        val formatter = appSettingsState.defaultLocalFormatter
        val inlayHintsPlaceEndOfLineEnabled = appSettingsState.isInlayHintsPlaceEndOfLineEnable

        findUnixTimestamp(text)
            .flatMap { word ->
                findTextRanges(
                    text,
                    word
                ).map { textRange -> word to textRange }
            }  // Pairing each word with its range
            .forEach { (word, textRange) ->
                val offset = if (inlayHintsPlaceEndOfLineEnabled) textRange.endOffset else textRange.startOffset
                if (uniqueIndices.add(offset)) {
                    val value = dropLastChar(word)
                    val instant = createInstantFormat(value)
                    val hint = formatter.format(instant)

                    sink.addPresentation(
                        InlineInlayPosition(offset, false), hasBackground = true
                    ) {
                        text(hint)
                    }
                }
            }
    }
}
