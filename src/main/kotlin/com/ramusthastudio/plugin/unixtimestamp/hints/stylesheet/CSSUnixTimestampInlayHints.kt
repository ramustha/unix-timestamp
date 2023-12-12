package com.ramusthastudio.plugin.unixtimestamp.hints.stylesheet

import com.intellij.psi.PsiFile
import com.intellij.psi.css.CssFile
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints

class CSSUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = CssFile::class.java
}
