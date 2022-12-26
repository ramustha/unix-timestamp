package com.ramusthastudio.plugin.unixtimestamp.hints.database;

import com.intellij.codeInsight.hints.InlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.sql.psi.SqlFile;
import com.ramusthastudio.plugin.unixtimestamp.hints.BaseInlayHintsCollector;
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints;
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SQLUnixTimestampInlayHints extends PlainTextUnixTimestampInlayHints {

  @Nullable
  @Override
  public InlayHintsCollector getCollectorFor(@NotNull PsiFile file,
      @NotNull Editor editor,
      @NotNull AppSettingsState settingsState,
      @NotNull InlayHintsSink inlayHintsSink) {
    return new BaseInlayHintsCollector(editor, settingsState, SqlFile.class);
  }

  @Override
  public boolean isLanguageSupported(@NotNull Language language) {
    return language.getID().toUpperCase().contains("SQL")
        || "Sybase".equals(language.getID())
        || "BigQuery".equals(language.getID())
        || "CassandraQL".equals(language.getID())
        || "ClickHouse".equals(language.getID())
        || "CouchbaseQuery".equals(language.getID())
        || "DB2_IS".equals(language.getID())
        || "DB2".equals(language.getID())
        || "Derby".equals(language.getID())
        || "Exasol".equals(language.getID())
        || "H2".equals(language.getID())
        || "HiveQL".equals(language.getID())
        || "MongoDB".equals(language.getID())
        || "AZURE".equals(language.getID())
        || "MariaDB".equals(language.getID())
        || "Oracle".equals(language.getID())
        || "Cockroach".equals(language.getID())
        || "Greenplum".equals(language.getID())
        || "Redshift".equals(language.getID())
        || "Redis".equals(language.getID())
        || "Snowflake".equals(language.getID())
        || "Vertica".equals(language.getID());
  }
}
