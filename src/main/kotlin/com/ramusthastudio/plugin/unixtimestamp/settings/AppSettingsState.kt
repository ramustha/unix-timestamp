package com.ramusthastudio.plugin.unixtimestamp.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@State(
    name = "com.ramusthastudio.plugin.unixtimestamp.settings.UnixTimestampSettingsState",
    storages = [Storage("UnixTimestampSettingsPlugin.xml")]
)
class AppSettingsState private constructor() : PersistentStateComponent<AppSettingsState> {
    var isInlayHintsPlaceEndOfLineEnable = true
    var isCurrentTimestampGeneratorEnable = true
    var isCustomTimestampGeneratorEnable = true
    var isSupportMicroSecondsEnable = true
    var isSupportNanoSecondsEnable = true
    var customPattern = "dd MMM yyyy HH:mm:ss"
    var defaultLocalFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(customPattern)
    var zoneId: String = ZoneId.systemDefault().id

    companion object {
        val instance: AppSettingsState
            get() = ApplicationManager.getApplication().getService(AppSettingsState::class.java)
    }

    override fun getState(): AppSettingsState {
        return this
    }

    override fun loadState(state: AppSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
        applySettings()
    }

    override fun noStateLoaded() {
        super.noStateLoaded()
        applySettings()
    }

    fun applySettings() {
        defaultLocalFormatter = DateTimeFormatter
            .ofPattern(customPattern)
            .withZone(zoneId.let { ZoneId.of(it) } ?: ZoneId.systemDefault())
    }

    override fun toString(): String {
        return "AppSettingsState(" +
                "isInlayHintsPlaceEndOfLineEnable=$isInlayHintsPlaceEndOfLineEnable, " +
                "isCurrentTimestampGeneratorEnable=$isCurrentTimestampGeneratorEnable, " +
                "isCustomTimestampGeneratorEnable=$isCustomTimestampGeneratorEnable, " +
                "isSupportMicroSecondsEnable=$isSupportMicroSecondsEnable, " +
                "isSupportNanoSecondsEnable=$isSupportNanoSecondsEnable, " +
                "customPattern='$customPattern', " +
                "defaultLocalFormatter=$defaultLocalFormatter, " +
                "zoneId='$zoneId'" +
                ")"
    }
}
