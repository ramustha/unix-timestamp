package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.InlineInlayPosition
import com.intellij.codeInsight.hints.declarative.HintFormat
import com.intellij.codeInsight.hints.declarative.OwnBypassCollector
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiFile
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.createInstantFormat
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper.findUnixTimestamp

class BaseInlayHintsCollector(
    private val editor: Editor,
    private val psiFileClass: Class<out PsiFile>,
    private val settingsState: AppSettingsState = AppSettingsState.instance
) : OwnBypassCollector {
    override fun collectHintsForFile(file: PsiFile, sink: InlayTreeSink) {
        ProgressManager.checkCanceled()
        val document = editor.document
        if (!psiFileClass.isInstance(file) || document.textLength > MAX_DOCUMENT_LENGTH) {
            return
        }

        findUnixTimestamp(
            document.charsSequence,
            settingsState.isSupportMicroSecondsEnable,
            settingsState.isSupportNanoSecondsEnable,
            MAX_HINTS_PER_FILE
        ).forEachIndexed { index, (word, textRange) ->
            if (index % CANCELLATION_CHECK_INTERVAL == 0) {
                ProgressManager.checkCanceled()
            }

            val instant = createInstantFormat(word) ?: return@forEachIndexed
            val hint = settingsState.defaultLocalFormatter.format(instant)
            val offset = if (settingsState.isInlayHintsPlaceEndOfLineEnable) {
                textRange.endOffset
            } else {
                textRange.startOffset
            }
            sink.addPresentation(
                InlineInlayPosition(offset, false),
                emptyList(),
                null,
                HintFormat.default
            ) { text(hint) }
        }
    }

    companion object {
        internal const val MAX_DOCUMENT_LENGTH = 2_000_000
        internal const val MAX_HINTS_PER_FILE = 1_000
        private const val CANCELLATION_CHECK_INTERVAL = 128
    }
}
