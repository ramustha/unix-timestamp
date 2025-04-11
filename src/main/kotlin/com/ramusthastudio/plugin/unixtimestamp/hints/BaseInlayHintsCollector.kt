package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.InlineInlayPosition
import com.intellij.codeInsight.hints.declarative.SharedBypassCollector
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.createInstantFormat
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.findUnixTimestamp

class BaseInlayHintsCollector<T : PsiElement?>(
    val file: PsiFile,
    val editor: Editor,
    private val psiElement: Class<T>,
    private val settingsState: AppSettingsState = AppSettingsState.instance
) : SharedBypassCollector {
    override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
        if (psiElement.isInstance(element)) {
            val uniqueIndices: MutableSet<Int> = mutableSetOf()
            val textRange = element.textRange
            val document = editor.document
            val text = document.getText(textRange)
            
            findUnixTimestamp(text)
                .forEach { (word, range) -> 
                    // Convert relative range to absolute range
                    val absoluteRange = TextRange(
                        textRange.startOffset + range.startOffset,
                        textRange.startOffset + range.endOffset
                    )
                    addTextPresentation(uniqueIndices, absoluteRange, word, sink)
                }
        }
    }

    private fun addTextPresentation(
        uniqueIndices: MutableSet<Int>,
        textRange: TextRange,
        word: String,
        sink: InlayTreeSink
    ) {
        val offset = if (settingsState.isInlayHintsPlaceEndOfLineEnable) textRange.endOffset else textRange.startOffset
        if (uniqueIndices.add(offset)) {
            val instant = createInstantFormat(word)
            val hint = settingsState.defaultLocalFormatter.format(instant)
            sink.addPresentation(InlineInlayPosition(offset, false), hasBackground = true) { text(hint) }
        }
    }
}
