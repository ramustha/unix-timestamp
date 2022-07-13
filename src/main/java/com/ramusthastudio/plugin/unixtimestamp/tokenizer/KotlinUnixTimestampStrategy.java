package com.ramusthastudio.plugin.unixtimestamp.tokenizer;

import com.intellij.psi.PsiElement;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.psi.KtFile;

public class KotlinUnixTimestampStrategy extends UnixTimestampStrategy {

  @Override
  public Tokenizer getTokenizer(PsiElement element) {
    if (element instanceof KtFile) {
      return TIME_MILLIS_TOKENIZER;
    }
    return super.getTokenizer(element);
  }

  @Override
  public boolean isLanguageSupported(@NotNull PsiElement element) {
    return "kotlin".equals(element.getLanguage().getID());
  }
}
