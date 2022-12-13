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
  private boolean isCurrentTimestampGeneratorEnable = true;
  private boolean isCustomTimestampGeneratorEnable = true;
  private String customPattern = null;
  private DateTimeFormatter formatter;

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
    return formatter;
  }

  private void createEffectiveFormatter() {
    formatter = dateFormatSettings.getValue();
    if (isCustomPatternEnable) {
      formatter = DateTimeFormatter.ofPattern(customPattern);
    }
    formatter = formatter.withZone(isUtcEnable ? ZoneId.of("UTC") : ZoneId.systemDefault());
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

  public boolean isCurrentTimestampGeneratorEnable() {
    return isCurrentTimestampGeneratorEnable;
  }

  public void setCurrentTimestampGeneratorEnable(boolean currentTimestampGeneratorEnable) {
    this.isCurrentTimestampGeneratorEnable = currentTimestampGeneratorEnable;
  }

  public boolean isCustomTimestampGeneratorEnable() {
    return isCustomTimestampGeneratorEnable;
  }

  public void setCustomTimestampGeneratorEnable(boolean customTimestampGeneratorEnable) {
    this.isCustomTimestampGeneratorEnable = customTimestampGeneratorEnable;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("dateFormatSettings", dateFormatSettings)
        .append("isCustomPatternEnable", isCustomPatternEnable)
        .append("isUtcEnable", isUtcEnable)
        .append("isInlayHintsPlaceEndOfLineEnable", isInlayHintsPlaceEndOfLineEnable)
        .append("showTimestampGenerator", isCurrentTimestampGeneratorEnable)
        .append("showCustomTimestampGenerator", isCustomTimestampGeneratorEnable)
        .append("customPattern", customPattern)
        .toString();
  }
}
