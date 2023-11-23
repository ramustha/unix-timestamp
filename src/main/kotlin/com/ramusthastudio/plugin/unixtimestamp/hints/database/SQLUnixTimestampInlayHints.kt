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
        return compareLanguage(
            language,
            "SQL",
            "Sybase",
            "BigQuery",
            "CassandraQL",
            "ClickHouse",
            "CouchbaseQuery",
            "DB2_IS",
            "DB2",
            "Derby",
            "Exasol",
            "H2",
            "HiveQL",
            "MongoDB",
            "AZURE",
            "MariaDB",
            "Oracle",
            "Cockroach",
            "Greenplum",
            "Redshift",
            "Redis",
            "Snowflake",
            "Vertica",
        )
    }
}
