package com.ramusthastudio.plugin.unixtimestamp.tokenizer;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiPlainText;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.spellchecker.inspections.PlainTextSplitter;
import com.intellij.spellchecker.tokenizer.TokenConsumer;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import com.intellij.spellchecker.tokenizer.TokenizerBase;
import com.intellij.util.KeyedLazyInstance;
import com.ramusthastudio.plugin.unixtimestamp.splitter.TimestampSplitter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class UnixTimestampStrategy {
  public static final ExtensionPointName<KeyedLazyInstance<UnixTimestampStrategy>> EP_NAME =
      new ExtensionPointName<>("com.ramusthastudio.plugin.unixtimestamp.ep.support");
  public static final Tokenizer<?> EMPTY_TOKENIZER = new Tokenizer<>() {
    @Override
    public void tokenize(@NotNull PsiElement element, TokenConsumer consumer) {
      // do nothing
    }

    @Override
    public String toString() {
      return "EMPTY_TOKENIZER";
    }
  };

  public static final Tokenizer<PsiElement> TIME_MILLIS_TOKENIZER =
      new TokenizerBase<>(TimestampSplitter.getInstance());

  private static final LocalQuickFix[] BATCH_FIXES = LocalQuickFix.EMPTY_ARRAY;

  @SuppressWarnings("all")
  @NotNull
  public Tokenizer getTokenizer(PsiElement element) {
    if (element instanceof XmlAttributeValue) {
      return TIME_MILLIS_TOKENIZER;
    }
    if (element instanceof PsiPlainText) {
      return TIME_MILLIS_TOKENIZER;
    }
    return EMPTY_TOKENIZER;
  }

  public LocalQuickFix[] getRegularFixes(PsiElement element,
      @NotNull TextRange textRange,
      boolean useRename,
      String typo) {
    return getDefaultRegularFixes(useRename, typo, element, textRange);
  }

  public static LocalQuickFix[] getDefaultRegularFixes(boolean useRename,
      String typo,
      @Nullable PsiElement element,
      @NotNull TextRange range) {
    return LocalQuickFix.EMPTY_ARRAY;
  }

  public static LocalQuickFix[] getDefaultBatchFixes() {
    return BATCH_FIXES;
  }

  public boolean isMyContext(@NotNull PsiElement element) {
    return "TEXT".equals(element.getLanguage().getID());
  }
}
