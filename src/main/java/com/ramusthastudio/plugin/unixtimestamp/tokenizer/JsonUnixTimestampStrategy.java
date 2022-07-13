package com.ramusthastudio.plugin.unixtimestamp.tokenizer;

import com.intellij.json.psi.JsonFile;
import com.intellij.psi.PsiElement;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;

public class JsonUnixTimestampStrategy extends UnixTimestampStrategy {

  @Override
  public Tokenizer getTokenizer(PsiElement element) {
    if (element instanceof JsonFile) {
      return TIME_MILLIS_TOKENIZER;
    }
    return super.getTokenizer(element);
  }

  @Override
  public boolean isLanguageSupported(@NotNull PsiElement element) {
    return "JSON".equals(element.getLanguage().getID());
  }
}
