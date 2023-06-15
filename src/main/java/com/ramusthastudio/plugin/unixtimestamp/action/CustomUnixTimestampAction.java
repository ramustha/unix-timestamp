package com.ramusthastudio.plugin.unixtimestamp.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState;
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class CustomUnixTimestampAction extends AnActionButton {
  private final AppSettingsState appSettingsState = AppSettingsState.getInstance();

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    GenerateTimestampDialog timestampDialog = new GenerateTimestampDialog(appSettingsState);
    if (timestampDialog.showAndGet()) {
      Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
      Project project = e.getRequiredData(CommonDataKeys.PROJECT);
      Document document = editor.getDocument();
      List<Caret> allCarets = editor.getCaretModel().getAllCarets();
      for (Caret caret : allCarets) {
        int start = caret.getSelectionStart();
        int end = caret.getSelectionEnd();
        WriteCommandAction.runWriteCommandAction(project,
            () -> document.replaceString(start, end, timestampDialog.getResult()));
      }
    }
  }

  @Override
  public void updateButton(@NotNull AnActionEvent event) {
    event.getPresentation().setEnabledAndVisible(appSettingsState.isCustomTimestampGeneratorEnable());
  }

  static class GenerateTimestampDialog extends DialogWrapper {
    private final AppSettingsState appSettingsState;
    private final JBTextField customDateTextField = new JBTextField("dd MMM yyyy HH:mm:ss", 20);
    private DateTimeFormatter formatter;

    GenerateTimestampDialog(AppSettingsState appSettingsState) {
      super(true);
      this.appSettingsState = appSettingsState;
      this.formatter = appSettingsState.getDefaultLocalFormatter();
      if (appSettingsState.isUtcEnable()) {
        this.formatter = this.formatter.withZone(ZoneId.of("UTC"));
      } else {
        this.formatter = this.formatter.withZone(ZoneId.systemDefault());
      }

      setTitle("Generate Custom Millis");
      init();
    }

    @Override
    protected void init() {
      super.init();

      EventQueue.invokeLater(() -> {
        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
        String localFormat = String.format("%s", formatter.format(instant));

        customDateTextField.setText(localFormat);
        customDateTextField.selectAll();
        customDateTextField.requestFocus();
      });
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
      try {
        getResult();
        return super.doValidate();
      } catch (Exception e) {
        return new ValidationInfo("Invalid format!", customDateTextField);
      }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
      Box inlayHintsBox = Box.createHorizontalBox();
      inlayHintsBox.add(customDateTextField);
      final JPanel inlayHintsPanel = new JPanel(new BorderLayout());
      inlayHintsPanel.add(inlayHintsBox, BorderLayout.NORTH);

      return FormBuilder.createFormBuilder()
          .addLabeledComponent(new JBLabel("Input date: "), inlayHintsPanel, 1)
          .addComponentFillVertically(new JPanel(), 0)
          .getPanel();
    }

    public String getResult() {
      return String.format("%s",
          Helper.createTimestamp(customDateTextField.getText(),
              formatter,
              appSettingsState.isUtcEnable()));
    }
  }
}
