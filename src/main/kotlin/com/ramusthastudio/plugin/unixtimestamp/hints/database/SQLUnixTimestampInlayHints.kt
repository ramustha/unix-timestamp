package com.ramusthastudio.plugin.unixtimestamp.hints.database

import com.intellij.codeInsight.hints.InlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.sql.psi.SqlFile
import com.ramusthastudio.plugin.unixtimestamp.hints.BaseInlayHintsCollector
import com.ramusthastudio.plugin.unixtimestamp.hints.PlainTextUnixTimestampInlayHints
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState

class SQLUnixTimestampInlayHints : PlainTextUnixTimestampInlayHints() {

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: AppSettingsState,
        sink: InlayHintsSink
    ): InlayHintsCollector {
        return BaseInlayHintsCollector(editor, settings, SqlFile::class.java)
    }

    override fun isLanguageSupported(language: Language): Boolean {
        return language.id.uppercase().contains("SQL")
                || "Sybase" == language.id
                || "BigQuery" == language.id
                || "CassandraQL" == language.id
                || "ClickHouse" == language.id
                || "CouchbaseQuery" == language.id
                || "DB2_IS" == language.id
                || "DB2" == language.id
                || "Derby" == language.id
                || "Exasol" == language.id
                || "H2" == language.id
                || "HiveQL" == language.id
                || "MongoDB" == language.id
                || "AZURE" == language.id
                || "MariaDB" == language.id
                || "Oracle" == language.id
                || "Cockroach" == language.id
                || "Greenplum" == language.id
                || "Redshift" == language.id
                || "Redis" == language.id
                || "Snowflake" == language.id
                || "Vertica" == language.id
    }
}
