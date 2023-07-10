package com.ramusthastudio.plugin.unixtimestamp.utils

import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.codeInsight.hints.presentation.PresentationFactory
import com.intellij.psi.PsiElement
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.createInlayHintsElement
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.createInstantFormat
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.createTimestamp
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.currentTimestamp
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.findTextRange
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.findUnixTimestamp
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.isMillisOrSecondsFormat
import junit.framework.TestCase
import org.junit.Assert
import org.mockito.Mockito
import java.time.Instant
import java.time.format.DateTimeFormatter

class HelperTest : TestCase() {
    fun testIsMillisOrSecondsFormat() {
        Assert.assertTrue(isMillisOrSecondsFormat("1670587272776"))
        Assert.assertTrue(isMillisOrSecondsFormat("1607446800000"))
        Assert.assertFalse(isMillisOrSecondsFormat("1670587"))
    }

    fun testCreateInstantFormat() {
        val instantFormatSeconds = createInstantFormat("1607446800")
        val instantFormatMillis = createInstantFormat("1607446800000")
        Assert.assertEquals(Instant.ofEpochSecond(1607446800), instantFormatSeconds)
        Assert.assertEquals(Instant.ofEpochMilli(1607446800000L), instantFormatMillis)
    }

    fun testCreateTimestamp() {
        val localTimestamp = createTimestamp("2022-12-12T00:00:00", DateTimeFormatter.ISO_DATE_TIME)
        Assert.assertEquals(1670778000000L, localTimestamp)
    }

    fun testCurrentTimestamp() {
        val localTimestamp = currentTimestamp()
        Assert.assertNotNull(localTimestamp)
    }

    fun testFindUnixTimestamp() {
        val instantFormatSeconds = findUnixTimestamp("1607446800")
        val instantFormatMillis = findUnixTimestamp("1607446800000")
        Assert.assertEquals(listOf("1607446800"), instantFormatSeconds)
        Assert.assertEquals(listOf("1607446800000"), instantFormatMillis)
    }

    fun testFindTextRange() {
        val instantFormatSeconds = findTextRange("1607446800", listOf("1607446800", "1607446800"))
        val instantFormatMillis = findTextRange("1607446800000", listOf("1607446800000", "1607446800000"))
        val instantFormatInvalid = findTextRange("1607446800000", listOf("16074468000001607446800000"))
        Assert.assertEquals(2, instantFormatSeconds.size.toLong())
        Assert.assertEquals(2, instantFormatMillis.size.toLong())
        Assert.assertEquals(0, instantFormatInvalid.size.toLong())
        Assert.assertEquals(10, instantFormatSeconds[0].length.toLong())
        Assert.assertEquals(13, instantFormatMillis[0].length.toLong())
    }

    fun testTestFindTextRange() {
        val instantFormatSeconds = findTextRange("1607446800", "1607446800, 1607446800000")
        val instantFormatMillis = findTextRange("1607446800000", "1607446800000, 1607446800")
        Assert.assertEquals(0, instantFormatSeconds.size.toLong())
        Assert.assertEquals(0, instantFormatMillis.size.toLong())
    }

    fun testCreateInlayHintsElement() {
        val psiElement = Mockito.mock(PsiElement::class.java)
        val inlayHintsSink = Mockito.mock(InlayHintsSink::class.java)
        val presentationFactory = Mockito.mock(
            PresentationFactory::class.java
        )
        val appSettingsState = Mockito.mock(AppSettingsState::class.java)
        Mockito.`when`(psiElement.text).thenReturn("1607446800000")
        Mockito.`when`(appSettingsState.defaultLocalFormatter).thenReturn(DateTimeFormatter.ISO_INSTANT)
        createInlayHintsElement(psiElement, inlayHintsSink, presentationFactory, appSettingsState)
    }
}
