package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.codeInsight.hints.InlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
import org.jetbrains.kotlin.psi.KtFile

class KotlinUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: AppSettingsState,
        sink: InlayHintsSink
    ): InlayHintsCollector {
        return BaseInlayHintsCollector(editor, settings, KtFile::class.java)
    }

    override fun isLanguageSupported(language: Language): Boolean {
        return compareLanguage(language, "kotlin")
    }
}
