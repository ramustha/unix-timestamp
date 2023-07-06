package com.ramusthastudio.plugin.unixtimestamp.settings;

import com.intellij.ui.components.JBCheckBox;
import junit.framework.TestCase;
import org.junit.Assert;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.ZoneId;

public class AppSettingsComponentTest extends TestCase {

  public void testActionPerformedSuccess() {
    AppSettingsComponent component = new AppSettingsComponent();
    Assert.assertNotNull(component);
    Assert.assertNotNull(component.getPanel());
    Assert.assertNotNull(component.getPreferredFocusedComponent());
    Assert.assertNotNull(component.getSelectedZoneId());
    Assert.assertFalse(component.isInvalid());
    Assert.assertFalse(component.isInlayHintsPlaceEndOfLineEnable());

    JBCheckBox checkBox = new JBCheckBox("Checkbox");
    component.actionPerformed(new ActionEvent(checkBox, 1, "custom"));

    JButton button = new JButton("Button");
    component.actionPerformed(new ActionEvent(button, 1, "custom"));
  }

  public void testActionPerformedFailed() {
    AppSettingsComponent component = new AppSettingsComponent();

    JBCheckBox checkBox = new JBCheckBox("Checkbox");
    component.actionPerformed(new ActionEvent(checkBox, 1, "custom"));

    JButton button = new JButton("Button");
    component.actionPerformed(new ActionEvent(button, 1, "custom"));
  }

  public void testActionPerformedInvalidFormat() {
    AppSettingsComponent component = new AppSettingsComponent();
    component.setCustomPattern("invalid");
    component.setSelectedZoneId(ZoneId.systemDefault().getId());
    component.setInlayHintsPlaceEndOfLineEnable(true);
    Assert.assertFalse(component.isAlreadyPreview());
    Assert.assertTrue(component.isInlayHintsPlaceEndOfLineEnable());
    Assert.assertNotNull(component.getCustomPattern());

    JBCheckBox checkBox = new JBCheckBox("Checkbox");
    component.actionPerformed(new ActionEvent(checkBox, 1, "custom"));

    JButton button = new JButton("Button");
    component.actionPerformed(new ActionEvent(button, 1, "custom"));
  }
}
