package com.ramusthastudio.plugin.unixtimestamp.tokenizer;

import com.intellij.lang.LanguageExtension;

public class LanguageUnixTimestamp extends LanguageExtension<UnixTimestampStrategy> {
  public static final LanguageUnixTimestamp INSTANCE = new LanguageUnixTimestamp();

  private LanguageUnixTimestamp() {
    super(UnixTimestampStrategy.EP_NAME, new UnixTimestampStrategy());
  }
}
