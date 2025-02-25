package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.SharedBypassCollector
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.createInlayHintsElement

class BaseInlayHintsCollector<T : PsiElement?>(
    val file: PsiFile,
    val editor: Editor,
    private val psiElement: Class<T>,
    private val settingsState: AppSettingsState = AppSettingsState.instance,
    private val uniqueIndices: MutableSet<Int> = FixedSizeSet(1000) // Limit to 1000 entries
) : SharedBypassCollector {
    override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
        if (psiElement.isInstance(element)) {
            createInlayHintsElement(uniqueIndices, element, sink, settingsState)
        }
    }
}

class FixedSizeSet<T>(private val maxSize: Int) : LinkedHashSet<T>() {
    override fun add(element: T): Boolean {
        if (size >= maxSize) remove(first()) // Remove the oldest element
        return super.add(element)
    }
}
