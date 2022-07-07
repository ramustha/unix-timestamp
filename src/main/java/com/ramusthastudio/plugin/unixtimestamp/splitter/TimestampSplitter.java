package com.ramusthastudio.plugin.unixtimestamp.splitter;

import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.util.TextRange;
import com.intellij.spellchecker.inspections.BaseSplitter;
import com.intellij.util.Consumer;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TimestampSplitter extends BaseSplitter {
  private static final TimestampSplitter INSTANCE = new TimestampSplitter();

  public static TimestampSplitter getInstance() {
    return INSTANCE;
  }

  private static final int MILLIS_LENGTH = 13;
  public static final int SECONDS_LENGTH = 10;

  @Override
  public void split(@Nullable String text, @NotNull TextRange range, Consumer<TextRange> consumer) {
    if (text == null || range.getLength() < MILLIS_LENGTH) {
      return;
    }

    try {
      String fixedText = text.replaceAll("\\D", " ");
      List<String> unixEpochCandidateList = Arrays.stream(fixedText.split("\\s+"))
          .filter(t -> (NumberUtils.isDigits(t) && isMillisOrSecondsFormat(t)))
          .distinct()
          .collect(Collectors.toList());
      for (String word : unixEpochCandidateList) {
        for (TextRange textRange : findWord(fixedText, word)) {
          consumer.consume(textRange);
        }
      }
    } catch (ProcessCanceledException ignored) {
      // ignored
    }
  }

  private static boolean isMillisOrSecondsFormat(String t) {
    return t.length() == MILLIS_LENGTH || t.length() == SECONDS_LENGTH;
  }

  private static List<TextRange> findWord(String text, String word) {
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
    return indexes.stream()
        .map(i -> new TextRange(i, i + word.length()))
        .distinct()
        .collect(Collectors.toList());
  }
}
