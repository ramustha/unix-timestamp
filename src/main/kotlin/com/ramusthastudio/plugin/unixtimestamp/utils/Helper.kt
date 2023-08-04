package com.ramusthastudio.plugin.unixtimestamp.utils

import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.codeInsight.hints.presentation.PresentationFactory
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

    fun isMillisOrSecondsFormat(value: String) = value.length == MILLIS_LENGTH || value.length == SECONDS_LENGTH

    fun currentTimestamp() = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

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
        val pattern = "\\b\\d{10,13}\\b".toRegex()
        return pattern.findAll(text)
            .map { it.value }
            .filter { it.length == SECONDS_LENGTH || it.length == MILLIS_LENGTH }
            .distinct()
            .toList()
    }

    fun findTextRanges(text: String, wordListOfWords: List<String>): List<TextRange> =
        wordListOfWords.flatMap { word -> findTextRanges(text, word) }

    fun findTextRanges(text: String, targetWord: String): List<TextRange> {
        val targetWordLength = targetWord.length
        val targetPositions = mutableSetOf<Int>()
        var searchStartIndex = 0

        while (true) {
            val foundIndex = text.indexOf(targetWord, searchStartIndex)
            if (foundIndex == -1) {
                break // Target word not found anymore
            }
            targetPositions.add(foundIndex)
            searchStartIndex = foundIndex + targetWordLength
        }

        return targetPositions.map { TextRange(it, it + targetWordLength) }
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
            .flatMap { word -> findTextRanges(text, word).map { textRange -> word to textRange } }  // Pairing each word with its range
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
