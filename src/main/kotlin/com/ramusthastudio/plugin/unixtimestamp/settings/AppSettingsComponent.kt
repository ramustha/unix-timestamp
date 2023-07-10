// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.ramusthastudio.plugin.unixtimestamp.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AppSettingsComponent implements ActionListener {

  private final JPanel mainPanel;
  private final ComboBox<String> zoneIdComboBox =
          new ComboBox<String>(new ListComboBoxModel<>(
                  ZoneId.getAvailableZoneIds().stream().sorted().toList()));
  private final JBTextField customPatternTextField = new JBTextField("dd MMM yyyy HH:mm:ss", 25);
  private final JButton previewButton = new JButton("Preview");
  private final JButton systemDefaultButton = new JButton("System default");
  private final JBCheckBox inlayHintsPlaceEndOfLineCheckBox = new JBCheckBox("Place at the end of line");
  private final JBCheckBox currentTimestampGeneratorCheckBox = new JBCheckBox("Current timestamp");
  private final JBCheckBox customTimestampGeneratorCheckBox = new JBCheckBox("Custom timestamp");
  private final JBLabel previewLabel = new JBLabel();
  private boolean alreadyPreview;
  private boolean isInvalid;

  public AppSettingsComponent() {
    Box generatorBox = Box.createHorizontalBox();
    generatorBox.add(currentTimestampGeneratorCheckBox);
    generatorBox.add(Box.createHorizontalStrut(5));
    generatorBox.add(customTimestampGeneratorCheckBox);
    final JPanel generatorPanel = new JPanel(new BorderLayout());
    generatorPanel.add(generatorBox, BorderLayout.NORTH);

    Box zoneIdBox = Box.createHorizontalBox();
    zoneIdBox.add(zoneIdComboBox);
    zoneIdBox.add(systemDefaultButton);
    final JPanel zoneIdPanel = new JPanel(new BorderLayout());
    zoneIdPanel.add(zoneIdBox, BorderLayout.NORTH);

    Box previewBox = Box.createHorizontalBox();
    previewBox.add(previewButton);
    previewBox.add(Box.createHorizontalStrut(5));
    previewBox.add(previewLabel);
    final JPanel previewPanel = new JPanel(new BorderLayout());
    previewPanel.add(previewBox, BorderLayout.NORTH);

    mainPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(new JBLabel("Position: "), inlayHintsPlaceEndOfLineCheckBox, 1)
        .addLabeledComponent(new JBLabel("Generator: "), generatorPanel, 1)
        .addSeparator(1)
        .addLabeledComponent(new JBLabel("Zone ID: "), zoneIdPanel, 1)
        .addLabeledComponent(new JBLabel("Date format: "), customPatternTextField, 1)
        .addComponentToRightColumn(previewPanel, 1)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    systemDefaultButton.addActionListener(this);
    previewButton.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() instanceof JButton button) {
      if (button.equals(systemDefaultButton)) {
        zoneIdComboBox.setSelectedItem(ZoneId.systemDefault().getId());
      }

      if (button.equals(previewButton)) {
        try {
          String customPattern = customPatternTextField.getText();
          DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(customPattern);
          dateTimeFormatter = dateTimeFormatter.withZone(ZoneId.of(getSelectedZoneId()));
          previewLabel.setText(dateTimeFormatter.format(ZonedDateTime.now()));
          previewLabel.setForeground(JBColor.foreground());
          isInvalid = false;
        } catch (Exception exception) {
          previewLabel.setText("Invalid format!");
          previewLabel.setForeground(JBColor.RED);
          isInvalid = true;
        }
        alreadyPreview = true;
      }
    }
  }

  public JPanel getPanel() {
    return mainPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return customPatternTextField;
  }

  public String getSelectedZoneId() {
    return (String) zoneIdComboBox.getSelectedItem();
  }

  public void setSelectedZoneId(String zoneId) {
    zoneIdComboBox.setSelectedItem(zoneId);
  }

  public String getCustomPattern() {
    return customPatternTextField.getText();
  }

  public void setCustomPattern(String pattern) {
    customPatternTextField.setText(pattern);
  }

  public boolean isAlreadyPreview() {
    return alreadyPreview;
  }

  public boolean isInvalid() {
    return isInvalid;
  }

  public boolean isInlayHintsPlaceEndOfLineEnable() {
    return inlayHintsPlaceEndOfLineCheckBox.isSelected();
  }

  public void setInlayHintsPlaceEndOfLineEnable(boolean selected) {
    inlayHintsPlaceEndOfLineCheckBox.setSelected(selected);
  }

  public boolean isCurrentTimestampGeneratorEnable() {
    return currentTimestampGeneratorCheckBox.isSelected();
  }

  public void setCurrentTimestampGeneratorEnable(boolean selected) {
    currentTimestampGeneratorCheckBox.setSelected(selected);
  }

  public boolean isCustomTimestampGeneratorEnable() {
    return customTimestampGeneratorCheckBox.isSelected();
  }

  public void setCustomTimestampGeneratorEnable(boolean selected) {
    customTimestampGeneratorCheckBox.setSelected(selected);
  }
}
