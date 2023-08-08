// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.ramusthastudio.plugin.unixtimestamp.settings

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.JBColor
import com.intellij.ui.MutableCollectionComboBoxModel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.swing.Box
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel

class AppSettingsComponent : ActionListener {
    private val zoneIdComboBox = ComboBox(
        MutableCollectionComboBoxModel(ZoneId.getAvailableZoneIds().stream().sorted().toList())
    )
    private val customPatternTextField = JBTextField("dd MMM yyyy HH:mm:ss", 25)
    private val previewButton = JButton("Preview")
    private val systemDefaultButton = JButton("System default")
    private val inlayHintsPlaceEndOfLineCheckBox = JBCheckBox("Place at the end of line")
    private val currentTimestampGeneratorCheckBox = JBCheckBox("Current timestamp")
    private val customTimestampGeneratorCheckBox = JBCheckBox("Custom timestamp")
    private val previewLabel = JBLabel()
    val panel: JPanel
    var isAlreadyPreview = false
    var isInvalid = false

    init {
        val generatorBox = Box.createHorizontalBox()
        generatorBox.add(currentTimestampGeneratorCheckBox)
        generatorBox.add(Box.createHorizontalStrut(5))
        generatorBox.add(customTimestampGeneratorCheckBox)
        val generatorPanel = JPanel(BorderLayout())
        generatorPanel.add(generatorBox, BorderLayout.NORTH)
        val zoneIdBox = Box.createHorizontalBox()
        zoneIdBox.add(zoneIdComboBox)
        zoneIdBox.add(systemDefaultButton)
        val zoneIdPanel = JPanel(BorderLayout())
        zoneIdPanel.add(zoneIdBox, BorderLayout.NORTH)
        val previewBox = Box.createHorizontalBox()
        previewBox.add(previewButton)
        previewBox.add(Box.createHorizontalStrut(5))
        previewBox.add(previewLabel)
        val previewPanel = JPanel(BorderLayout())
        previewPanel.add(previewBox, BorderLayout.NORTH)

        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Position: "), inlayHintsPlaceEndOfLineCheckBox, 1)
            .addLabeledComponent(JBLabel("Generator: "), generatorPanel, 1)
            .addSeparator(1)
            .addLabeledComponent(JBLabel("Zone ID: "), zoneIdPanel, 1)
            .addLabeledComponent(JBLabel("Date format: "), customPatternTextField, 1)
            .addComponentToRightColumn(previewPanel, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
        systemDefaultButton.addActionListener(this)
        previewButton.addActionListener(this)
    }

    override fun actionPerformed(e: ActionEvent) {
        if (e.source is JButton) {
            if (e.source == systemDefaultButton) {
                zoneIdComboBox.setSelectedItem(ZoneId.systemDefault().id)
            }
            if (e.source == previewButton) {
                try {
                    val customPattern = customPatternTextField.getText()
                    var dateTimeFormatter = DateTimeFormatter.ofPattern(customPattern)
                    dateTimeFormatter = dateTimeFormatter.withZone(ZoneId.of(selectedZoneId))
                    previewLabel.setText(dateTimeFormatter.format(ZonedDateTime.now()))
                    previewLabel.setForeground(JBColor.foreground())
                    isInvalid = false
                } catch (exception: Exception) {
                    previewLabel.setText("Invalid format!")
                    previewLabel.setForeground(JBColor.RED)
                    isInvalid = true
                }
                isAlreadyPreview = true
            }
        }
    }

    val preferredFocusedComponent: JComponent
        get() = customPatternTextField
    var selectedZoneId: String?
        get() = zoneIdComboBox.selectedItem as String
        set(zoneId) {
            zoneIdComboBox.setSelectedItem(zoneId)
        }
    var customPattern: String?
        get() = customPatternTextField.getText()
        set(pattern) {
            customPatternTextField.setText(pattern)
        }
    var isInlayHintsPlaceEndOfLineEnable: Boolean
        get() = inlayHintsPlaceEndOfLineCheckBox.isSelected
        set(selected) {
            inlayHintsPlaceEndOfLineCheckBox.setSelected(selected)
        }
    var isCurrentTimestampGeneratorEnable: Boolean
        get() = currentTimestampGeneratorCheckBox.isSelected
        set(selected) {
            currentTimestampGeneratorCheckBox.setSelected(selected)
        }
    var isCustomTimestampGeneratorEnable: Boolean
        get() = customTimestampGeneratorCheckBox.isSelected
        set(selected) {
            customTimestampGeneratorCheckBox.setSelected(selected)
        }
}
