package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.codeInsight.hints.declarative.InlayHintsCollector
import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiPlainTextFile

open class PlainTextUnixTimestampInlayHints : InlayHintsProvider {

    override fun createCollector(
        file: PsiFile,
        editor: Editor
    ): InlayHintsCollector {
        return BaseInlayHintsCollector(file, editor, psiFile())
    }

    open fun psiFile(): Class<out PsiFile> = PsiPlainTextFile::class.java
}
