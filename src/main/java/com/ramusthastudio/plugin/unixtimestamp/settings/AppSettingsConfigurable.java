// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.ramusthastudio.plugin.unixtimestamp.settings;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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
    boolean modified;
    if (mySettingsComponent.isCustomPatternEnable()) {
      modified = mySettingsComponent.isAlreadyPreview()
          && !mySettingsComponent.getCustomPattern().equals(settings.getCustomPattern());
    } else {
      modified = !mySettingsComponent.getSelectedItem().equals(settings.getDateFormatSettings());
    }
    modified |= mySettingsComponent.isUtcEnable() != settings.isUtcEnable();
    modified |= mySettingsComponent.isInlayHintsPlaceEndOfLineEnable() != settings.isInlayHintsPlaceEndOfLineEnable();
    return modified;
  }

  @Override
  public void apply() throws ConfigurationException {
    if (mySettingsComponent.isInvalid()) {
      throw new ConfigurationException("Invalid format!");
    }

    AppSettingsState settings = AppSettingsState.getInstance();
    if (mySettingsComponent.isCustomPatternEnable()) {
      String patternText = mySettingsComponent.getCustomPattern();
      settings.setCustomPatternEnable(true);
      settings.setCustomPattern(patternText);
    } else {
      DateFormatSettings selected = mySettingsComponent.getSelectedItem();
      settings.setCustomPatternEnable(false);
      settings.setDateFormatSettings(selected);
    }
    settings.setUtcEnable(mySettingsComponent.isUtcEnable());
    settings.setInlayHintsPlaceEndOfLineEnable(mySettingsComponent.isInlayHintsPlaceEndOfLineEnable());
    LOG.debug("apply settings = " + settings);
  }

  @Override
  public void reset() {
    AppSettingsState settings = AppSettingsState.getInstance();
    if (settings.isCustomPatternEnable()) {
      String patternText = settings.getCustomPattern();
      mySettingsComponent.setCustomPatternEnable(true);
      mySettingsComponent.setCustomPattern(patternText);
      mySettingsComponent.enableDefaultFormat(false);
    } else {
      DateFormatSettings savedState = settings.getDateFormatSettings();
      mySettingsComponent.setCustomPatternEnable(false);
      mySettingsComponent.setSelectedItem(savedState);
      mySettingsComponent.enableDefaultFormat(true);
    }
    mySettingsComponent.setUtcEnable(settings.isUtcEnable());
    mySettingsComponent.setInlayHintsPlaceEndOfLineEnable(settings.isInlayHintsPlaceEndOfLineEnable());
  }

  @Override
  public void disposeUIResources() {
    mySettingsComponent = null;
  }

}
