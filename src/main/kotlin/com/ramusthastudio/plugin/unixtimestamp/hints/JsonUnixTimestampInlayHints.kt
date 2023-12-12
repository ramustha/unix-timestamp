package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.json.psi.JsonFile
import com.intellij.psi.PsiFile

class JsonUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = JsonFile::class.java
}
