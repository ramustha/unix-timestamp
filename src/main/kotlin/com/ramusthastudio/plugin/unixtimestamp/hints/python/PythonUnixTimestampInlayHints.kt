package com.ramusthastudio.plugin.unixtimestamp.hints.python

import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyFile
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints

class PythonUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = PyFile::class.java
}
