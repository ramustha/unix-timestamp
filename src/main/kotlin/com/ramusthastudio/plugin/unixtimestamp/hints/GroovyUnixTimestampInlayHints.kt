package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.psi.PsiFile
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile

class GroovyUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = GroovyFile::class.java
}
