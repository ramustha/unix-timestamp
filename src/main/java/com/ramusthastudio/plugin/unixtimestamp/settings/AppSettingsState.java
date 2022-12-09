package com.ramusthastudio.plugin.unixtimestamp.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@State(name = "com.ramusthastudio.plugin.unixtimestamp.settings.UnixTimestampSettingsState",
    storages = @Storage("UnixTimestampSettingsPlugin.xml"))
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {
  private DateFormatSettings dateFormatSettings = DateFormatSettings.RFC_1123_DATE_TIME;
  private boolean isCustomPatternEnable;
  private boolean isUtcEnable;
  private boolean isInlayHintsPlaceEndOfLineEnable = true;
  private String customPattern = null;
  private DateTimeFormatter effectiveFormatter;

  public static AppSettingsState getInstance() {
    return ApplicationManager.getApplication().getService(AppSettingsState.class);
  }

  @Nullable
  @Override
  public AppSettingsState getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull AppSettingsState state) {
    XmlSerializerUtil.copyBean(state, this);
    createEffectiveFormatter();
  }

  @Override
  public void noStateLoaded() {
    PersistentStateComponent.super.noStateLoaded();
    createEffectiveFormatter();
  }

  public DateTimeFormatter getDefaultLocalFormatter() {
    return effectiveFormatter;
  }

  private void createEffectiveFormatter() {
    effectiveFormatter = dateFormatSettings.getValue();
    try {
      if (isCustomPatternEnable) {
        effectiveFormatter = DateTimeFormatter.ofPattern(customPattern);
      }
    } catch (Exception e) {
      // ignored
    }
    effectiveFormatter = effectiveFormatter.withZone(isUtcEnable ? ZoneId.of("UTC") : ZoneId.systemDefault());
  }

  public DateTimeFormatter getDefaultUtcFormatter() {
    try {
      if (isCustomPatternEnable) {
        return DateTimeFormatter.ofPattern(customPattern).withZone(ZoneId.of("UTC"));
      }
    } catch (Exception e) {
      // ignored
    }
    return dateFormatSettings.getValue().withZone(ZoneId.of("UTC"));
  }

  public DateFormatSettings getDateFormatSettings() {
    return dateFormatSettings;
  }

  public void setDateFormatSettings(DateFormatSettings dateFormatSettings) {
    this.dateFormatSettings = dateFormatSettings;
    createEffectiveFormatter();
  }

  public String getCustomPattern() {
    return customPattern;
  }

  public void setCustomPattern(String customPattern) {
    this.customPattern = customPattern;
    createEffectiveFormatter();
  }

  public void setCustomPatternEnable(boolean customPatternEnable) {
    isCustomPatternEnable = customPatternEnable;
    createEffectiveFormatter();
  }

  public boolean isCustomPatternEnable() {
    return isCustomPatternEnable;
  }

  public boolean isUtcEnable() {
    return isUtcEnable;
  }

  public void setUtcEnable(boolean utcEnable) {
    isUtcEnable = utcEnable;
    createEffectiveFormatter();
  }

  public boolean isInlayHintsPlaceEndOfLineEnable() {
    return isInlayHintsPlaceEndOfLineEnable;
  }

  public void setInlayHintsPlaceEndOfLineEnable(boolean inlayHintsPlaceEndOfLineEnable) {
    isInlayHintsPlaceEndOfLineEnable = inlayHintsPlaceEndOfLineEnable;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("dateFormatSettings", dateFormatSettings)
        .append("isCustomPatternEnable", isCustomPatternEnable)
        .append("isUtcEnable", isUtcEnable)
        .append("isInlayHintsPlaceEndOfLineEnable", isInlayHintsPlaceEndOfLineEnable)
        .append("customPattern", customPattern)
        .append("effectiveFormatter", effectiveFormatter)
        .toString();
  }
}
