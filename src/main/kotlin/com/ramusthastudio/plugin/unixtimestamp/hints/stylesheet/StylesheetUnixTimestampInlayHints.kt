package com.ramusthastudio.plugin.unixtimestamp.hints.stylesheet

import com.intellij.psi.PsiFile
import com.intellij.psi.css.impl.StylesheetFileBase
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints

class StylesheetUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = StylesheetFileBase::class.java
}
