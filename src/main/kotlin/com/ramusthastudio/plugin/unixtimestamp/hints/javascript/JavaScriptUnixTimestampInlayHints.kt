package com.ramusthastudio.plugin.unixtimestamp.hints.javascript

import com.intellij.lang.javascript.psi.JSFile
import com.intellij.psi.PsiFile
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints

class JavaScriptUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = JSFile::class.java
}
