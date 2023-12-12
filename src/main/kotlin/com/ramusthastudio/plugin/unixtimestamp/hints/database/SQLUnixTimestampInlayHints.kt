package com.ramusthastudio.plugin.unixtimestamp.hints.database

import com.intellij.psi.PsiFile
import com.intellij.sql.psi.SqlFile
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints

class SQLUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = SqlFile::class.java
}
