package com.ramusthastudio.plugin.unixtimestamp.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CurrentUnixTimestampAction extends AnAction {
  private final AppSettingsState appSettingsState = AppSettingsState.getInstance();

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
    Project project = e.getRequiredData(CommonDataKeys.PROJECT);
    Document document = editor.getDocument();
    List<Caret> allCarets = editor.getCaretModel().getAllCarets();
    for (Caret caret : allCarets) {
      int start = caret.getSelectionStart();
      int end = caret.getSelectionEnd();
      WriteCommandAction.runWriteCommandAction(project,
          () -> document.replaceString(start, end, String.valueOf(System.currentTimeMillis())));
    }
  }
  @Override
  public void update(@NotNull AnActionEvent event) {
    event.getPresentation().setEnabledAndVisible(appSettingsState.isCurrentTimestampGeneratorEnable());
  }
}
