package com.ramusthastudio.plugin.unixtimestamp.hints.javascript

import com.intellij.codeInsight.hints.InlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.lang.Language
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.ramusthastudio.plugin.unixtimestamp.hints.BaseInlayHintsCollector
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState

class TypeScriptJSXUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: AppSettingsState,
        sink: InlayHintsSink
    ): InlayHintsCollector {
        return BaseInlayHintsCollector(editor, settings, JSFile::class.java)
    }

    override fun isLanguageSupported(language: Language): Boolean {
        return "TypeScript JSX" == language.id
    }
}
