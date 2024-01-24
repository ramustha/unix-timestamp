package com.ramusthastudio.plugin.unixtimestamp.hints.php

import com.intellij.psi.PsiFile
import com.jetbrains.php.lang.psi.PhpFile
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints

class PhpUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = PhpFile::class.java
}
