package com.ramusthastudio.plugin.unixtimestamp.utils;

import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.codeInsight.hints.presentation.InlayPresentation;
import com.intellij.codeInsight.hints.presentation.PresentationFactory;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Helper {
  public static final int MILLIS_LENGTH = 13;
  public static final int SECONDS_LENGTH = 10;

  private Helper() {
  }

  public static boolean isMillisOrSecondsFormat(String t) {
    return t.length() == MILLIS_LENGTH || t.length() == SECONDS_LENGTH;
  }

  public static Instant createInstantFormat(String longValue) {
    if (longValue.length() == SECONDS_LENGTH) {
      return Instant.ofEpochSecond(Long.parseLong(longValue));
    }
    return Instant.ofEpochMilli(Long.parseLong(longValue));
  }

  public static long createTimestamp(String value, DateTimeFormatter formatter, boolean isUtc) {
    LocalDateTime localDateTime = LocalDateTime.parse(value, formatter);
    Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    if (isUtc) {
      instant = localDateTime.atZone(ZoneId.of("UTC")).toInstant();
    }
    return instant.toEpochMilli();
  }

  public static long currentTimestamp(boolean isUtc) {
    LocalDateTime localDateTime = LocalDateTime.now();
    Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    if (isUtc) {
      instant = localDateTime.atZone(ZoneId.of("UTC")).toInstant();
    }
    return instant.toEpochMilli();
  }

  public static List<String> findUnixTimestamp(String text) {
    String fixedText = text.replaceAll("\\D", " ");
    List<String> list = new ArrayList<>();
    Set<String> uniqueValues = new HashSet<>();
    for (String t : fixedText.split("\\s+")) {
      if ((NumberUtils.isDigits(t) && Helper.isMillisOrSecondsFormat(t))) {
        if (uniqueValues.add(t)) {
          list.add(t);
        }
      }
    }
    return list;
  }

  public static List<TextRange> findTextRange(String text, List<String> wordOfList) {
    List<TextRange> textRangeList = Lists.newArrayList();
    for (String word : wordOfList) {
      textRangeList.addAll(findTextRange(text, word));
    }
    return textRangeList;
  }

  public static List<TextRange> findTextRange(String text, String word) {
    List<Integer> indexes = new ArrayList<>();
    int wordLength = 0;
    int index = 0;
    while (index != -1) {
      index = text.indexOf(word, index + wordLength);
      if (index != -1) {
        indexes.add(index);
      }
      wordLength = word.length();
    }
    List<TextRange> list = new ArrayList<>();
    Set<TextRange> uniqueValues = new HashSet<>();
    for (Integer i : indexes) {
      TextRange textRange = new TextRange(i, i + word.length());
      if (uniqueValues.add(textRange)) {
        list.add(textRange);
      }
    }
    return list;
  }

  public static void createInlayHintsElement(@NotNull PsiElement element,
      @NotNull InlayHintsSink sink,
      PresentationFactory factory,
      AppSettingsState appSettingsState) {
    String text = element.getText();

    List<String> unixEpochCandidateList = Helper.findUnixTimestamp(text);
    for (String word : unixEpochCandidateList) {
      for (TextRange textRange : Helper.findTextRange(text, word)) {

        Instant instant = Helper.createInstantFormat(word);
        DateTimeFormatter formatter = appSettingsState.getDefaultLocalFormatter();
        String localFormat = String.format("%s", formatter.format(instant));

        PresentationFactory scaleAwareFactory = factory;
        InlayPresentation inlayPresentation =
            scaleAwareFactory.roundWithBackgroundAndSmallInset(factory.smallText(localFormat));

        sink.addInlineElement(textRange.getStartOffset(),
            true,
            inlayPresentation,
            appSettingsState.isInlayHintsPlaceEndOfLineEnable());
      }
    }
  }
}
