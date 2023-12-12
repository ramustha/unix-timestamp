package com.ramusthastudio.plugin.unixtimestamp.hints.go

import com.goide.psi.GoFile
import com.intellij.psi.PsiFile
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints

class GoUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = GoFile::class.java
}
