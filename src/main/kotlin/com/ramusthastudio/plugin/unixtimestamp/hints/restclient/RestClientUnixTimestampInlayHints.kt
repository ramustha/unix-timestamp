package com.ramusthastudio.plugin.unixtimestamp.hints.restclient

import com.intellij.httpClient.http.request.HttpRequestPsiFile
import com.intellij.psi.PsiFile
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints

class RestClientUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = HttpRequestPsiFile::class.java
}
