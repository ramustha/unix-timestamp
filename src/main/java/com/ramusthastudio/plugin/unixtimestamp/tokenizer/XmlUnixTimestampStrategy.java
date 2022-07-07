package com.ramusthastudio.plugin.unixtimestamp.tokenizer;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

public class XmlUnixTimestampStrategy extends UnixTimestampStrategy {

  @Override
  public Tokenizer getTokenizer(PsiElement element) {
    if (element instanceof XmlFile) {
      return TIME_MILLIS_TOKENIZER;
    }
    return super.getTokenizer(element);
  }

  @Override
  public boolean isMyContext(@NotNull PsiElement element) {
    return "XML".equals(element.getLanguage().getID()) || "XHTML".equals(element.getLanguage()
        .getID()) || "HTML".equals(element.getLanguage().getID());
  }
}
