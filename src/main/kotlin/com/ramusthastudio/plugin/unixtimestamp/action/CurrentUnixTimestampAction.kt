package com.ramusthastudio.plugin.unixtimestamp.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState

class CurrentUnixTimestampAction : AnAction() {
    private val appSettingsState = AppSettingsState.instance

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val project = e.getRequiredData(CommonDataKeys.PROJECT)
        val document = editor.document
        val allCarets = editor.caretModel.allCarets
        val currentTime = System.currentTimeMillis().toString()

        for (caret in allCarets) {
            val start = caret.selectionStart
            val end = caret.selectionEnd
            WriteCommandAction.runWriteCommandAction(project) { document.replaceString(start, end, currentTime) }
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = appSettingsState.isCurrentTimestampGeneratorEnable
    }
}
