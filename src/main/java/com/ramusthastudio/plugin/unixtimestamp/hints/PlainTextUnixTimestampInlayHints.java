package com.ramusthastudio.plugin.unixtimestamp.hints;

import com.intellij.codeInsight.hints.ImmediateConfigurable;
import com.intellij.codeInsight.hints.InlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsProvider;
import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.codeInsight.hints.SettingsKey;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPlainTextFile;
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlainTextUnixTimestampInlayHints implements InlayHintsProvider<AppSettingsState> {
  private static final String UNIX_TIMESTAMP_HINTS = "UnixTimestampHints";
  private static final SettingsKey<AppSettingsState> KEY = new SettingsKey<>(UNIX_TIMESTAMP_HINTS);

  @Nullable
  @Override
  public InlayHintsCollector getCollectorFor(@NotNull PsiFile file,
      @NotNull Editor editor,
      @NotNull AppSettingsState settingsState,
      @NotNull InlayHintsSink inlayHintsSink) {
    return new BaseInlayHintsCollector(editor, settingsState, PsiPlainTextFile.class);
  }

  @NotNull
  @Override
  public AppSettingsState createSettings() {
    return AppSettingsState.getInstance();
  }

  @Nls(capitalization = Nls.Capitalization.Sentence)
  @NotNull
  @Override
  public String getName() {
    return UNIX_TIMESTAMP_HINTS;
  }

  @NotNull
  @Override
  public SettingsKey<AppSettingsState> getKey() {
    return KEY;
  }

  @Override
  public String getPreviewText() {
    return null;
  }

  @Override
  public ImmediateConfigurable createConfigurable(@NotNull AppSettingsState settings) {
    return null;
  }

  @Override
  public boolean isLanguageSupported(@NotNull Language language) {
    return "TEXT".equals(language.getID());
  }

  @Override
  public boolean isVisibleInSettings() {
    return false;
  }
}
