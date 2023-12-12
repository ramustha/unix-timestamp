package com.ramusthastudio.plugin.unixtimestamp.hints.xml

import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlFile
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints

class XmlUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {
    override fun psiFile(): Class<out PsiFile> = XmlFile::class.java
}
