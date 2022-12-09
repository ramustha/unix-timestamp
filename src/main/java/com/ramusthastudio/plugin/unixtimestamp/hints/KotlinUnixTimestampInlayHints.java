package com.ramusthastudio.plugin.unixtimestamp.hints;

import com.intellij.codeInsight.hints.FactoryInlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState;
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.psi.KtFile;

public class KotlinUnixTimestampInlayHints extends UnixTimestampInlayHints {

  @Nullable
  @Override
  public InlayHintsCollector getCollectorFor(@NotNull PsiFile file,
      @NotNull Editor editor,
      @NotNull AppSettingsState settingsState,
      @NotNull InlayHintsSink inlayHintsSink) {
    return new FactoryInlayHintsCollector(editor) {
      @Override
      public boolean collect(@NotNull PsiElement element, @NotNull Editor editor, @NotNull InlayHintsSink sink) {
        if (element instanceof KtFile) {
          Helper.createInlayHintsElement(element, sink, getFactory(), settingsState);
          return true;
        }
        return false;
      }
    };
  }

  @Override
  public boolean isLanguageSupported(@NotNull Language language) {
    return "kotlin".equals(language.getID());
  }
}
