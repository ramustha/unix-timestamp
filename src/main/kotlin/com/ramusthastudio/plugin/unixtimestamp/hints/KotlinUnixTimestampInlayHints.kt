package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.KtFile

class KotlinUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = KtFile::class.java
}
