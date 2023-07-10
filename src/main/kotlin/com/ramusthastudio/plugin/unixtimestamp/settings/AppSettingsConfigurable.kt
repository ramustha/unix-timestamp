// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.ramusthastudio.plugin.unixtimestamp.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class AppSettingsConfigurable : Configurable {
    private var mySettingsComponent: AppSettingsComponent? = null

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return "Unix Epoch Time Visualize"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return mySettingsComponent?.preferredFocusedComponent
    }

    override fun createComponent(): JComponent? {
        mySettingsComponent = AppSettingsComponent()
        return mySettingsComponent?.panel
    }

    override fun isModified(): Boolean {
        val settings = AppSettingsState.instance

        var modified = mySettingsComponent?.isAlreadyPreview ?: false
        if (mySettingsComponent?.customPattern != null) {
            modified = modified or (mySettingsComponent?.customPattern != settings.customPattern)
        }
        if (mySettingsComponent?.selectedZoneId != null) {
            modified = modified or (mySettingsComponent?.selectedZoneId != settings.zoneId)
        }
        modified =
            modified or (mySettingsComponent?.isInlayHintsPlaceEndOfLineEnable != settings.isInlayHintsPlaceEndOfLineEnable)
        modified =
            modified or (mySettingsComponent?.isCurrentTimestampGeneratorEnable != settings.isCurrentTimestampGeneratorEnable)
        modified =
            modified or (mySettingsComponent?.isCustomTimestampGeneratorEnable != settings.isCustomTimestampGeneratorEnable)
        return modified
    }

    override fun apply() {
        if (mySettingsComponent?.isInvalid == true) {
            throw ConfigurationException("Invalid format!")
        }

        val settings = AppSettingsState.instance
        settings.customPattern = mySettingsComponent?.customPattern.toString()
        settings.zoneId = mySettingsComponent?.selectedZoneId.toString()
        settings.isInlayHintsPlaceEndOfLineEnable = mySettingsComponent?.isInlayHintsPlaceEndOfLineEnable == true
        settings.isCurrentTimestampGeneratorEnable = mySettingsComponent?.isCurrentTimestampGeneratorEnable == true
        settings.isCustomTimestampGeneratorEnable = mySettingsComponent?.isCustomTimestampGeneratorEnable == true
        settings.applySettings()
    }

    override fun reset() {
        val settings = AppSettingsState.instance
        mySettingsComponent?.customPattern = settings.customPattern
        mySettingsComponent?.selectedZoneId = settings.zoneId
        mySettingsComponent?.isInlayHintsPlaceEndOfLineEnable = settings.isInlayHintsPlaceEndOfLineEnable
        mySettingsComponent?.isCurrentTimestampGeneratorEnable = settings.isCurrentTimestampGeneratorEnable
        mySettingsComponent?.isCustomTimestampGeneratorEnable = settings.isCustomTimestampGeneratorEnable
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}
