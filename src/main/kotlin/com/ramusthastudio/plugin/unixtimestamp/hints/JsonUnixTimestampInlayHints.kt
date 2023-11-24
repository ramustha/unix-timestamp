package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.codeInsight.hints.InlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.json.psi.JsonFile
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState

class JsonUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: AppSettingsState,
        sink: InlayHintsSink
    ): InlayHintsCollector {
        return BaseInlayHintsCollector(editor, settings, JsonFile::class.java)
    }

    override fun isLanguageSupported(language: Language): Boolean {
        return compareLanguage(language, "JSON")
    }
}
