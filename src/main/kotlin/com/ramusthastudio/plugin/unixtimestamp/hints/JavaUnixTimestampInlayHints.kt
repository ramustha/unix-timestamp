package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile

class JavaUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = PsiJavaFile::class.java
}
