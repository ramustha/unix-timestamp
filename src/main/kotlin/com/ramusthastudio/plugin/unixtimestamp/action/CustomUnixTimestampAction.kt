package com.ramusthastudio.plugin.unixtimestamp.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.util.Computable
import com.intellij.ui.AnActionButton
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper
import java.awt.BorderLayout
import java.awt.EventQueue
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.swing.Box
import javax.swing.JComponent
import javax.swing.JPanel

class CustomUnixTimestampAction : AnActionButton() {
    private val appSettingsState = AppSettingsState.instance

    override fun actionPerformed(e: AnActionEvent) {
        val timestampDialog = GenerateTimestampDialog(appSettingsState)
        if (timestampDialog.showAndGet()) {
            val editor = e.getRequiredData(CommonDataKeys.EDITOR)
            val project = e.getRequiredData(CommonDataKeys.PROJECT)
            val document = editor.document
            val allCarets = editor.caretModel.allCarets
            val currentTime = timestampDialog.result

            for (caret in allCarets) {
                val start = caret.selectionStart
                val end = caret.selectionEnd
                WriteCommandAction.runWriteCommandAction(project, Computable {
                    document.replaceString(start, end, currentTime)
                })
            }
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun updateButton(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = appSettingsState.isCustomTimestampGeneratorEnable
    }

    internal class GenerateTimestampDialog(appSettingsState: AppSettingsState) : DialogWrapper(true) {
        private val customDateTextField = JBTextField("dd MMM yyyy HH:mm:ss", 20)
        private var formatter: DateTimeFormatter

        init {
            formatter = appSettingsState.defaultLocalFormatter
            formatter = formatter.withZone(ZoneId.of(appSettingsState.zoneId))
            title = "Generate Custom Millis"
            init()
        }

        override fun init() {
            super.init()
            EventQueue.invokeLater {
                val instant = Instant.ofEpochMilli(System.currentTimeMillis())
                val localFormat = String.format("%s", formatter.format(instant))
                customDateTextField.text = localFormat
                customDateTextField.selectAll()
                customDateTextField.requestFocus()
            }
        }

        override fun doValidate(): ValidationInfo? {
            return try {
                result
                super.doValidate()
            } catch (e: Exception) {
                ValidationInfo("Invalid format!", customDateTextField)
            }
        }

        override fun createCenterPanel(): JComponent? {
            val inlayHintsBox = Box.createHorizontalBox()
            inlayHintsBox.add(customDateTextField)
            val inlayHintsPanel = JPanel(BorderLayout())
            inlayHintsPanel.add(inlayHintsBox, BorderLayout.NORTH)

            return FormBuilder.createFormBuilder()
                .addLabeledComponent(JBLabel("Input date: "), inlayHintsPanel, 1)
                .addComponentFillVertically(JPanel(), 0)
                .panel
        }

        val result: String
            get() = String.format("%s", Helper.createTimestamp(customDateTextField.text, formatter))
    }
}
