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
import java.util.Optional;

@State(name = "com.ramusthastudio.plugin.unixtimestamp.settings.UnixTimestampSettingsState",
    storages = @Storage("UnixTimestampSettingsPlugin.xml"))
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {
  private boolean isInlayHintsPlaceEndOfLineEnable = true;
  private boolean isCurrentTimestampGeneratorEnable = true;
  private boolean isCustomTimestampGeneratorEnable = true;
  private DateTimeFormatter formatter;
  private String customPattern;
  private String zoneId;

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
    applySettings();
  }

  @Override
  public void noStateLoaded() {
    PersistentStateComponent.super.noStateLoaded();
    applySettings();
  }

  public DateTimeFormatter getDefaultLocalFormatter() {
    return formatter;
  }

  public void applySettings() {
    formatter = DateTimeFormatter
            .ofPattern(Optional.ofNullable(customPattern).orElse("dd MMM yyyy HH:mm:ss"))
            .withZone(Optional.ofNullable(zoneId).map(ZoneId::of).orElse(ZoneId.systemDefault()));
  }

  public String getCustomPattern() {
    return customPattern;
  }

  public void setCustomPattern(String customPattern) {
    this.customPattern = customPattern;
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

  public String getZoneId() {
    return zoneId;
  }

  public void setZoneId(String zoneId) {
    this.zoneId = zoneId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("zoneId", zoneId)
        .append("isInlayHintsPlaceEndOfLineEnable", isInlayHintsPlaceEndOfLineEnable)
        .append("showTimestampGenerator", isCurrentTimestampGeneratorEnable)
        .append("showCustomTimestampGenerator", isCustomTimestampGeneratorEnable)
        .append("customPattern", customPattern)
        .toString();
  }
}
