package com.ramusthastudio.plugin.unixtimestamp.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState;
import com.ramusthastudio.plugin.unixtimestamp.splitter.TimestampSplitter;
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;

public class UnixTimestampAction extends AnAction {
  private final AppSettingsState appSettingsState = AppSettingsState.getInstance();

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    final Project project = e.getProject();
    if (null == project || !project.isInitialized() || project.isDisposed()) {
      return;
    }

    final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
    final CaretModel caretModel = editor.getCaretModel();
    final Caret primaryCaret = caretModel.getPrimaryCaret();
    if (primaryCaret.getSelectedText() == null) {
      return;
    }

    String selectedText = primaryCaret.getSelectedText().replaceAll("\\D", "").trim();
    if (NumberUtils.isDigits(selectedText)
        && Helper.isMillisOrSecondsFormat(selectedText)) {
      Instant instant = Helper.createInstantFormat(selectedText);
      String localFormat = String.format("%s [SYS]",
          appSettingsState.getDefaultLocalFormatter()
              .withZone(ZoneId.systemDefault())
              .format(instant));
      String utcFormat = String.format("%s [UTC]",
          appSettingsState.getDefaultUtcFormatter().format(instant));
      String message = String.format("%s%n%s", utcFormat, localFormat);

      Messages.showInfoMessage(e.getProject(), message, "Unix Timestamp");
    }
  }
}
