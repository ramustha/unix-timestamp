// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.ramusthastudio.plugin.unixtimestamp.settings;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.time.ZoneId;

public class AppSettingsConfigurable implements Configurable {
  private static final Logger LOG = Logger.getInstance(AppSettingsConfigurable.class);

  private AppSettingsComponent mySettingsComponent;

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "Unix Epoch Time Visualize";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return mySettingsComponent.getPreferredFocusedComponent();
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    mySettingsComponent = new AppSettingsComponent();
    return mySettingsComponent.getPanel();
  }

  @Override
  public boolean isModified() {
    AppSettingsState settings = AppSettingsState.getInstance();
    boolean modified = mySettingsComponent.isAlreadyPreview();
    if (mySettingsComponent.getCustomPattern() != null) {
      modified |= !mySettingsComponent.getCustomPattern().equals(settings.getCustomPattern());
    }
    if (mySettingsComponent.getSelectedZoneId() != null) {
      modified |= !mySettingsComponent.getSelectedZoneId().equals(settings.getZoneId());
    }

    modified |= mySettingsComponent.isInlayHintsPlaceEndOfLineEnable() != settings.isInlayHintsPlaceEndOfLineEnable();
    modified |= mySettingsComponent.isCurrentTimestampGeneratorEnable() != settings.isCurrentTimestampGeneratorEnable();
    modified |= mySettingsComponent.isCustomTimestampGeneratorEnable() != settings.isCustomTimestampGeneratorEnable();
    return modified;
  }

  @Override
  public void apply() throws ConfigurationException {
    if (mySettingsComponent.isInvalid()) {
      throw new ConfigurationException("Invalid format!");
    }

    AppSettingsState settings = AppSettingsState.getInstance();
    String patternText = mySettingsComponent.getCustomPattern();
    settings.setCustomPattern(patternText);
    settings.setZoneId(mySettingsComponent.getSelectedZoneId());
    settings.setInlayHintsPlaceEndOfLineEnable(mySettingsComponent.isInlayHintsPlaceEndOfLineEnable());
    settings.setCurrentTimestampGeneratorEnable(mySettingsComponent.isCurrentTimestampGeneratorEnable());
    settings.setCustomTimestampGeneratorEnable(mySettingsComponent.isCustomTimestampGeneratorEnable());
    settings.applySettings();
    LOG.debug("apply settings = " + settings);
  }

  @Override
  public void reset() {
    AppSettingsState settings = AppSettingsState.getInstance();
    mySettingsComponent.setCustomPattern(settings.getCustomPattern());
    mySettingsComponent.setSelectedZoneId(settings.getZoneId());
    mySettingsComponent.setInlayHintsPlaceEndOfLineEnable(settings.isInlayHintsPlaceEndOfLineEnable());
    mySettingsComponent.setCurrentTimestampGeneratorEnable(settings.isCurrentTimestampGeneratorEnable());
    mySettingsComponent.setCustomTimestampGeneratorEnable(settings.isCustomTimestampGeneratorEnable());
  }

  @Override
  public void disposeUIResources() {
    mySettingsComponent = null;
  }

}
