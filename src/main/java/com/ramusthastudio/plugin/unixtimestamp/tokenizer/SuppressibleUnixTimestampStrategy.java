package com.ramusthastudio.plugin.unixtimestamp.tokenizer;

import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public abstract class SuppressibleUnixTimestampStrategy extends UnixTimestampStrategy {
  public abstract boolean isSuppressedFor(@NotNull PsiElement element, @NotNull String name);

  public abstract SuppressQuickFix[] getSuppressActions(@NotNull PsiElement element,
      @NotNull String name);
}
