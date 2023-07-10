package com.ramusthastudio.plugin.unixtimestamp.utils

import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.codeInsight.hints.presentation.PresentationFactory
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.jgoodies.common.base.Strings
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
import org.apache.commons.lang.math.NumberUtils
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
        val fixedText = text.replace("\\D".toRegex(), " ")
        val list: MutableList<String> = ArrayList()
        val uniqueValues: MutableSet<String> = HashSet()

        for (t in fixedText.split("\\s+".toRegex())) {
            if (uniqueValues.add(t)
                && !Strings.isBlank(t)
                && NumberUtils.isDigits(t)
                && isMillisOrSecondsFormat(t)
            ) {
                list.add(t)
            }
        }
        return list
    }

    fun findTextRange(text: String, wordOfList: List<String>): List<TextRange> {
        return wordOfList.stream()
            .flatMap { findTextRange(text, it).stream() }
            .toList()
    }

    fun findTextRange(text: String, word: String): List<TextRange> {
        val indexes: MutableSet<Int> = HashSet()
        var wordLength = 0
        var index = 0
        while (index != -1) {
            index = text.indexOf(word, index + wordLength)
            if (index != -1) {
                indexes.add(index)
            }
            wordLength = word.length
        }
        val list: MutableList<TextRange> = ArrayList()
        val uniqueValues: MutableSet<TextRange> = HashSet()

        for (i in indexes) {
            val textRange = TextRange(i, i + word.length)
            if (uniqueValues.add(textRange)) {
                list.add(textRange)
            }
        }
        return list
    }

    fun createInlayHintsElement(
        element: PsiElement,
        sink: InlayHintsSink,
        factory: PresentationFactory,
        appSettingsState: AppSettingsState
    ) {
        val text = element.text
        val uniqueIndex: MutableSet<Int> = HashSet()
        val formatter = appSettingsState.defaultLocalFormatter
        val inlayHintsPlaceEndOfLineEnable = appSettingsState.isInlayHintsPlaceEndOfLineEnable

        for (word in findUnixTimestamp(text)) {
            val instant = createInstantFormat(word)
            val localFormat = String.format("%s", formatter.format(instant))
            val inlayPresentation = factory.roundWithBackgroundAndSmallInset(factory.smallText(localFormat))
            for (textRange in findTextRange(text, word)) {
                if (uniqueIndex.add(textRange.startOffset)) {
                    sink.addInlineElement(
                        textRange.startOffset,
                        true,
                        inlayPresentation,
                        inlayHintsPlaceEndOfLineEnable
                    )
                }
            }
        }
    }
}
