// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.ramusthastudio.plugin.unixtimestamp.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AppSettingsComponent implements ActionListener {

  private final JPanel mainPanel;
  private final ComboBox<DateFormatSettings> patternComboBox =
      new ComboBox<DateFormatSettings>(new EnumComboBoxModel<DateFormatSettings>(DateFormatSettings.class));
  private final JBTextField customPatternTextField = new JBTextField("dd MMM yyyy HH:mm:ss", 25);
  private final JButton previewButton = new JButton("Preview");
  private final JBCheckBox customPatternCheckBox = new JBCheckBox("Custom format: ");
  private final JBCheckBox utcCheckBox = new JBCheckBox("UTC");
  private final JBLabel previewLabel = new JBLabel();
  private boolean alreadyPreview;
  private boolean isInvalid;

  public AppSettingsComponent() {
    Box patternBox = Box.createHorizontalBox();
    patternBox.add(patternComboBox);
    patternBox.add(utcCheckBox);
    final JPanel patternPanel = new JPanel(new BorderLayout());
    patternPanel.add(patternBox, BorderLayout.NORTH);

    Box previewBox = Box.createHorizontalBox();
    previewBox.add(previewButton);
    previewBox.add(Box.createHorizontalStrut(5));
    previewBox.add(previewLabel);
    final JPanel previewPanel = new JPanel(new BorderLayout());
    previewPanel.add(previewBox, BorderLayout.NORTH);

    mainPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(new JBLabel("Date format: "), patternPanel, 1)
        .addSeparator(1)
        .addLabeledComponent(customPatternCheckBox, customPatternTextField, 1)
        .addComponentToRightColumn(previewPanel, 1)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    enableDefaultFormat(true);
    customPatternCheckBox.addActionListener(this);
    previewButton.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() instanceof JBCheckBox) {
      JBCheckBox checkBox = (JBCheckBox) e.getSource();
      enableDefaultFormat(!checkBox.isSelected());
    }

    if (e.getSource() instanceof JButton) {
      try {
        String customPattern = customPatternTextField.getText();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(customPattern);
        if (isUtcEnable()) {
          dateTimeFormatter = dateTimeFormatter.withZone(ZoneId.of("UTC"));
        } else {
          dateTimeFormatter = dateTimeFormatter.withZone(ZoneId.systemDefault());
        }
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

  public void enableDefaultFormat(boolean value) {
    customPatternTextField.setEditable(!value);
    previewButton.setEnabled(!value);
    patternComboBox.setEnabled(value);
    previewLabel.setText(null);
  }

  public JPanel getPanel() {
    return mainPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return patternComboBox;
  }

  @NotNull
  public DateFormatSettings getSelectedItem() {
    return (DateFormatSettings) Objects.requireNonNull(patternComboBox.getSelectedItem());
  }

  public void setSelectedItem(DateFormatSettings selectedItem) {
    patternComboBox.setSelectedItem(selectedItem);
  }

  public String getCustomPattern() {
    return customPatternTextField.getText();
  }

  public void setCustomPattern(String pattern) {
    customPatternTextField.setText(pattern);
  }

  public boolean isCustomPatternEnable() {
    return customPatternCheckBox.isSelected();
  }

  public void setCustomPatternEnable(boolean selected) {
    customPatternCheckBox.setSelected(selected);
  }

  public boolean isAlreadyPreview() {
    return alreadyPreview;
  }

  public boolean isInvalid() {
    return isInvalid;
  }

  public boolean isUtcEnable() {
    return utcCheckBox.isSelected();
  }

  public void setUtcEnable(boolean selected) {
    utcCheckBox.setSelected(selected);
  }
}
