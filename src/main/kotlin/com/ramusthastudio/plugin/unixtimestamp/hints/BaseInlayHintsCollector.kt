package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.codeInsight.hints.FactoryInlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.createInlayHintsElement

class BaseInlayHintsCollector<T : PsiElement?>(
    editor: Editor,
    private val settingsState: AppSettingsState,
    private val psiElement: Class<T>
) : FactoryInlayHintsCollector(editor) {
    override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
        if (psiElement.isInstance(element)) {
            createInlayHintsElement(element, sink, factory, settingsState)
            return true
        }
        return false
    }
}
