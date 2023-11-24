package com.ramusthastudio.plugin.unixtimestamp.utils

import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.codeInsight.hints.presentation.PresentationFactory
import com.intellij.database.util.common.lastCharIs
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Helper {
    private const val MILLIS_LENGTH = 13
    private const val SECONDS_LENGTH = 10

    fun createInstantFormat(longValue: String): Instant {
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
            .map { dropLastChar(it) }
            .filter { it.length == SECONDS_LENGTH || it.length == MILLIS_LENGTH }
            .distinct()
            .toList()
    }

    private fun dropLastChar(it: MatchResult): String {
        if (it.value.lastCharIs('l') or it.value.lastCharIs('L')) {
            return it.value.dropLast(1)
        }
        return it.value
    }

    fun findTextRanges(text: String, targetWord: String): List<TextRange> {
        return text.windowed(targetWord.length, 1)
            .withIndex()
            .filter { it.value == targetWord }
            .map { TextRange(it.index, it.index + targetWord.length) }
    }

    fun createInlayHintsElement(
        element: PsiElement,
        sink: InlayHintsSink,
        factory: PresentationFactory,
        appSettingsState: AppSettingsState
    ) {
        val text = element.text
        val uniqueIndices = mutableSetOf<Int>()
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
                if (uniqueIndices.add(textRange.startOffset)) {
                    val instant = createInstantFormat(word)
                    val localFormat = formatter.format(instant)
                    val inlayPresentation = factory.roundWithBackgroundAndSmallInset(factory.smallText(localFormat))
                    sink.addInlineElement(
                        textRange.startOffset,
                        true,
                        inlayPresentation,
                        inlayHintsPlaceEndOfLineEnabled
                    )
                }
            }
    }
}
