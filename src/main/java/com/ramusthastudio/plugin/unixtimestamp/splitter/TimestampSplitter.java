package com.ramusthastudio.plugin.unixtimestamp.splitter;

import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.util.TextRange;
import com.intellij.spellchecker.inspections.BaseSplitter;
import com.intellij.util.Consumer;
import com.ramusthastudio.plugin.unixtimestamp.utils.Helper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TimestampSplitter extends BaseSplitter {
  private static final TimestampSplitter INSTANCE = new TimestampSplitter();

  public static TimestampSplitter getInstance() {
    return INSTANCE;
  }

  @Override
  public void split(@Nullable String text, @NotNull TextRange range, Consumer<TextRange> consumer) {
    if (text == null || text.length() < Helper.MILLIS_LENGTH) {
      return;
    }

    try {
      List<String> unixEpochCandidateList = Helper.findUnixTimestamp(text);
      for (TextRange textRange : Helper.findTextRange(text, unixEpochCandidateList)) {
        consumer.consume(textRange);
      }
    } catch (ProcessCanceledException ignored) {
      // ignored
    }
  }
}
