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
    private const val MILLIS_LENGTH = 13
    private const val MILLIS_SUFFIX_LENGTH = 14
    private const val SECONDS_LENGTH = 10

    private fun createInstantFormat(longValue: String): Instant {
        if (longValue.length == SECONDS_LENGTH) {
            return Instant.ofEpochSecond(longValue.toLong())
        }
        return Instant.ofEpochMilli(longValue.toLong())
    }

    fun createTimestamp(value: String, formatter: DateTimeFormatter): Long {
        val localDateTime = LocalDateTime.parse(value, formatter)
        val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        return instant.toEpochMilli()
    }

    fun findUnixTimestamp(text: String): List<String> {
        val pattern = "\\b\\d{10,13}([lL])?\\b".toRegex()
        return pattern.findAll(text)
            .map { it.value }
            .filter {
                it.length == SECONDS_LENGTH || it.length == MILLIS_LENGTH || it.length == MILLIS_SUFFIX_LENGTH
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

    fun findTextRanges(text: String, targetWord: String): List<TextRange> {
        return text.windowed(targetWord.length, 1)
            .withIndex()
            .filter { it.value == targetWord }
            .map { TextRange(it.index, it.index + targetWord.length) }
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
