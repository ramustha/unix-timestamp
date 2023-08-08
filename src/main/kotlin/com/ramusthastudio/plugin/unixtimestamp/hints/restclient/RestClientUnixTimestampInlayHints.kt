package com.ramusthastudio.plugin.unixtimestamp.hints.restclient

import com.intellij.codeInsight.hints.InlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.httpClient.http.request.HttpRequestPsiFile
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.ramusthastudio.plugin.unixtimestamp.hints.BaseInlayHintsCollector
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState

class RestClientUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: AppSettingsState,
        sink: InlayHintsSink
    ): InlayHintsCollector {
        return BaseInlayHintsCollector(editor, settings, HttpRequestPsiFile::class.java)
    }

    override fun isLanguageSupported(language: Language): Boolean {
        return "HTTP Request".equals(language.id, ignoreCase = true)
    }
}
