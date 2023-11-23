package com.ramusthastudio.plugin.unixtimestamp.hints

import com.intellij.codeInsight.hints.ChangeListener
import com.intellij.codeInsight.hints.ImmediateConfigurable
import com.intellij.codeInsight.hints.InlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsProvider
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.codeInsight.hints.SettingsKey
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiPlainTextFile
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState
import javax.swing.JComponent
import javax.swing.JPanel

open class PlainTextUnixTimestampInlayHints : InlayHintsProvider<AppSettingsState> {
    private val UNIX_TIMESTAMP_HINTS = "UnixTimestampHints"
    private val KEY = SettingsKey<AppSettingsState>(UNIX_TIMESTAMP_HINTS)

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: AppSettingsState,
        sink: InlayHintsSink
    ): InlayHintsCollector {
        return BaseInlayHintsCollector(editor, settings, PsiPlainTextFile::class.java)
    }

    override fun createSettings(): AppSettingsState {
        return AppSettingsState.instance
    }

    override val name: String
        get() = UNIX_TIMESTAMP_HINTS

    override val key: SettingsKey<AppSettingsState>
        get() = KEY

    override fun isLanguageSupported(language: Language): Boolean {
        return "TEXT" == language.id
    }

    override val isVisibleInSettings: Boolean
        get() = false

    override val previewText: String?
        get() = null

    override fun createConfigurable(settings: AppSettingsState): ImmediateConfigurable {
        return object : ImmediateConfigurable {
            override fun createComponent(listener: ChangeListener): JComponent {
                return JPanel()
            }
        }
    }

    fun compareLanguage(language: Language, vararg languageIds: String): Boolean {
        return languageIds.any {
            println("selected language: $it")
            println("compiler language: $language")
            it.equals(language.id, ignoreCase = true)|| language.id.contains(it)
        }
    }
}
