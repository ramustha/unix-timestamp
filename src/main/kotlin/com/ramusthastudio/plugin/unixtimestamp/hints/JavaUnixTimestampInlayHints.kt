package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.codeInsight.hints.InlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState

class JavaUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: AppSettingsState,
        sink: InlayHintsSink
    ): InlayHintsCollector {
        return BaseInlayHintsCollector(editor, settings, PsiJavaFile::class.java)
    }

    override fun isLanguageSupported(language: Language): Boolean {
        return "JAVA" == language.id
    }
}
