package com.ramusthastudio.plugin.unixtimestamp.hints;

import com.intellij.codeInsight.hints.FactoryInlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState;
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper;
import org.jetbrains.annotations.NotNull;

public final class BaseInlayHintsCollector<T extends PsiElement> extends FactoryInlayHintsCollector {
  private final AppSettingsState settingsState;
  private final Class<T> psiElement;

  public BaseInlayHintsCollector(@NotNull Editor editor,
      AppSettingsState settingsState,
      Class<T> psiElement) {
    super(editor);
    this.settingsState = settingsState;
    this.psiElement = psiElement;
  }

  @Override
  public boolean collect(@NotNull PsiElement element, @NotNull Editor editor, @NotNull InlayHintsSink sink) {
    if (psiElement.isInstance(element)) {
      Helper.createInlayHintsElement(element, sink, getFactory(), settingsState);
      return true;
    }
    return false;
  }
}
