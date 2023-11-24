package com.ramusthastudio.plugin.unixtimestamp.hints.stylesheet

import com.intellij.codeInsight.hints.InlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.css.impl.StylesheetFileBase
import com.ramusthastudio.plugin.unixtimestamp.hints.BaseInlayHintsCollector
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState

class StylusUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: AppSettingsState,
        sink: InlayHintsSink
    ): InlayHintsCollector {
        return BaseInlayHintsCollector(editor, settings, StylesheetFileBase::class.java)
    }

    override fun isLanguageSupported(language: Language): Boolean {
        return compareLanguage(language, "Stylus")
    }
}
