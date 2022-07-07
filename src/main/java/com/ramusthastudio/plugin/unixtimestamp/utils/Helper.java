package com.ramusthastudio.plugin.unixtimestamp.utils;

import com.ramusthastudio.plugin.unixtimestamp.splitter.TimestampSplitter;

import java.time.Instant;

public final class Helper {
  private Helper() {
  }

  public static Instant createInstantFormat(String value) {
    if (value.length() == TimestampSplitter.SECONDS_LENGTH) {
      return Instant.ofEpochSecond(Long.parseLong(value));
    }
    return Instant.ofEpochMilli(Long.parseLong(value));
  }

}
