package com.ramusthastudio.plugin.unixtimestamp;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemDescriptorBase;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageNamesValidation;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.spellchecker.inspections.Splitter;
import com.intellij.spellchecker.tokenizer.TokenConsumer;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import com.intellij.util.Consumer;
import com.intellij.util.containers.CollectionFactory;
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState;
import com.ramusthastudio.plugin.unixtimestamp.tokenizer.LanguageUnixTimestamp;
import com.ramusthastudio.plugin.unixtimestamp.tokenizer.SuppressibleUnixTimestampStrategy;
import com.ramusthastudio.plugin.unixtimestamp.tokenizer.UnixTimestampStrategy;
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class UnixTimestampInspection extends LocalInspectionTool {
  private static final Logger LOG = LoggerFactory.getLogger(UnixTimestampInspection.class);
  private final AppSettingsState appSettingsState = AppSettingsState.getInstance();

  @Override
  public SuppressQuickFix @NotNull [] getBatchSuppressActions(@Nullable PsiElement element) {
    if (element != null) {
      final Language language = element.getLanguage();
      UnixTimestampStrategy strategy = getUnixEpochStrategy(element, language);
      if (strategy instanceof SuppressibleUnixTimestampStrategy) {
        return ((SuppressibleUnixTimestampStrategy) strategy).getSuppressActions(element,
            getShortName());
      }
    }
    return super.getBatchSuppressActions(element);
  }

  @Override
  public boolean isSuppressedFor(@NotNull PsiElement element) {
    final Language language = element.getLanguage();
    UnixTimestampStrategy strategy = getUnixEpochStrategy(element, language);
    if (strategy instanceof SuppressibleUnixTimestampStrategy) {
      return ((SuppressibleUnixTimestampStrategy) strategy).isSuppressedFor(element, getShortName());
    }
    return super.isSuppressedFor(element);
  }

  @Override
  public JComponent createOptionsPanel() {
    final Box verticalBox = Box.createVerticalBox();
    final JPanel panel = new JPanel(new BorderLayout());
    panel.add(verticalBox, BorderLayout.NORTH);
    return panel;
  }

  @Override
  public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
      boolean isOnTheFly) {
    return new PsiElementVisitor() {
      @Override
      public void visitElement(@NotNull PsiElement element) {
        if (appSettingsState.isInlayHintsEnable()) {
          return;
        }

        if (holder.getResultCount() > 1000)
          return;

        final ASTNode node = element.getNode();
        if (node == null) {
          return;
        }

        PsiFile containingFile = element.getContainingFile();
        if (containingFile != null && Boolean.TRUE.equals(containingFile.getUserData(
            InjectedLanguageManager.FRANKENSTEIN_INJECTION))) {
          return;
        }

        final Language language = element.getLanguage();
        tokenize(element,
            language,
            new CustomTokenConsumer(holder,
                LanguageNamesValidation.INSTANCE.forLanguage(language)));
      }
    };
  }

  @SuppressWarnings("all")
  public static void tokenize(@NotNull final PsiElement element,
      @NotNull final Language language,
      TokenConsumer consumer) {
    final UnixTimestampStrategy factoryByLanguage = getUnixEpochStrategy(element, language);
    if (factoryByLanguage == null)
      return;
    Tokenizer tokenizer = factoryByLanguage.getTokenizer(element);
    tokenizer.tokenize(element, consumer);
  }

  private static UnixTimestampStrategy getUnixEpochStrategy(@NotNull PsiElement element,
      @NotNull Language language) {
    for (UnixTimestampStrategy strategy : LanguageUnixTimestamp.INSTANCE.allForLanguage(language)) {
      if (strategy.isLanguageSupported(element)) {
        return strategy;
      }
    }
    return null;
  }

  static class CustomTokenConsumer extends TokenConsumer implements Consumer<TextRange> {
    private final Set<String> alreadyChecked = CollectionFactory.createSmallMemoryFootprintSet();
    private final AppSettingsState appSettingsState = AppSettingsState.getInstance();
    private final ProblemsHolder problemsHolder;
    private final NamesValidator namesValidator;
    private PsiElement psiElement;
    private String text;
    private boolean useRename;
    private int offset;

    CustomTokenConsumer(ProblemsHolder holder, NamesValidator namesValidator) {
      this.problemsHolder = holder;
      this.namesValidator = namesValidator;
    }

    @Override
    public void consumeToken(final PsiElement element,
        final String text,
        final boolean useRename,
        final int offset,
        TextRange rangeToCheck,
        Splitter splitter) {
      psiElement = element;
      this.text = text;
      this.useRename = useRename;
      this.offset = offset;
      splitter.split(text, rangeToCheck, this);
    }

    @SuppressWarnings("all")
    @Override
    public void consume(TextRange range) {
      String word = range.substring(text);

      if (!NumberUtils.isDigits(word)) {
        return;
      }

      if (!problemsHolder.isOnTheFly() && alreadyChecked.contains(word)) {
        return;
      }

      boolean keyword = namesValidator.isKeyword(word, psiElement.getProject());
      if (keyword) {
        return;
      }

      LOG.debug("language = {} range = {} word = {} {}",
          psiElement.getLanguage(),
          range,
          word,
          psiElement.getClass());

      UnixTimestampStrategy strategy = getUnixEpochStrategy(psiElement, psiElement.getLanguage());
      final Tokenizer tokenizer = strategy != null ? strategy.getTokenizer(psiElement) : null;
      if (tokenizer != null) {
        range = tokenizer.getHighlightingRange(psiElement, offset, range);
      }

      Instant instant = Helper.createInstantFormat(word);
      DateTimeFormatter formatter = appSettingsState.getDefaultLocalFormatter();
      String localFormat = String.format("%s", formatter.format(instant));
      if (problemsHolder.isOnTheFly()) {
        addRegularDescriptor(psiElement, range, problemsHolder, useRename, localFormat);
      } else {
        alreadyChecked.add(word);
        addBatchDescriptor(psiElement, range, problemsHolder, localFormat);
      }
    }

    private static void addBatchDescriptor(PsiElement element,
        @NotNull TextRange textRange,
        @NotNull ProblemsHolder holder,
        String word) {
      LocalQuickFix[] fixes = UnixTimestampStrategy.getDefaultBatchFixes();
      ProblemDescriptor problemDescriptor =
          createProblemDescriptor(element, textRange, fixes, false, word);
      holder.registerProblem(problemDescriptor);
    }

    private static void addRegularDescriptor(PsiElement element,
        @NotNull TextRange textRange,
        @NotNull ProblemsHolder holder,
        boolean useRename,
        String word) {
      LocalQuickFix[] fixes =
          UnixTimestampStrategy.getDefaultRegularFixes(useRename, word, element, textRange);
      final ProblemDescriptor problemDescriptor =
          createProblemDescriptor(element, textRange, fixes, true, word);
      holder.registerProblem(problemDescriptor);
    }

    private static ProblemDescriptor createProblemDescriptor(PsiElement element,
        TextRange textRange,
        LocalQuickFix[] fixes,
        boolean onTheFly,
        String dateWord) {
      return new ProblemDescriptorBase(element,
          element,
          dateWord,
          null,
          ProblemHighlightType.INFORMATION,
          false,
          textRange,
          onTheFly,
          onTheFly);
    }
  }
}
