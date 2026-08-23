package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.jna.JnaLoader
import com.intellij.openapi.diagnostic.Logger
import com.intellij.testFramework.utils.inlays.declarative.DeclarativeInlayHintsProviderTestCase
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState

abstract class UnixTimestampInlayHintsTestBase : DeclarativeInlayHintsProviderTestCase() {
    init {
        // The test harness does not run the IDE startup sequence that normally
        // initializes the JNA bridge, which macOS editors require.
        JnaLoader.load(Logger.getInstance(UnixTimestampInlayHintsTestBase::class.java))
    }

    override fun setUp() {
        super.setUp()
        // Pin the zone so the expected hint text does not depend on the machine's timezone.
        AppSettingsState.instance.apply {
            zoneId = "UTC"
            applySettings()
        }
    }

    protected fun assertTimestampHintRendered(fileName: String, provider: InlayHintsProvider) {
        // 1671160204198 millis == 2022-12-16T03:10:04Z, formatted with the default
        // "dd MMM yyyy HH:mm:ss" pattern in the UTC zone pinned above.
        doTestProvider(
            fileName,
            "1671160204198/*<# 16 Dec 2022 03:10:04 #>*/",
            provider
        )
    }
}
