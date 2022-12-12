package com.ramusthastudio.plugin.unixtimestamp.hints.javascript;

import com.intellij.codeInsight.hints.InlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.lang.Language;
import com.intellij.lang.javascript.psi.JSFile;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.ramusthastudio.plugin.unixtimestamp.hints.BaseInlayHintsCollector;
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints;
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavaScriptUnixTimestampInlayHints extends PlainTextUnixTimestampInlayHints {

  @Nullable
  @Override
  public InlayHintsCollector getCollectorFor(@NotNull PsiFile file,
      @NotNull Editor editor,
      @NotNull AppSettingsState settingsState,
      @NotNull InlayHintsSink inlayHintsSink) {
    return new BaseInlayHintsCollector(editor, settingsState, JSFile.class);
  }

  @Override
  public boolean isLanguageSupported(@NotNull Language language) {
    return language.getID().contains("ECMAScript");
  }
}
