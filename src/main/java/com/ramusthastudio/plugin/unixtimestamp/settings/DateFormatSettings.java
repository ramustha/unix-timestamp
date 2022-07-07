package com.ramusthastudio.plugin.unixtimestamp.settings;

import java.time.format.DateTimeFormatter;

enum DateFormatSettings {
  ISO_TIME(DateTimeFormatter.ISO_TIME, "10:15:30+01:00'; '10:15:30"),
  ISO_INSTANT(DateTimeFormatter.ISO_INSTANT, "2011-12-03T10:15:30Z"),
  ISO_DATE_TIME(DateTimeFormatter.ISO_DATE_TIME, "2011-12-03T10:15:30+01:00[Europe/Paris]"),
  RFC_1123_DATE_TIME(DateTimeFormatter.RFC_1123_DATE_TIME, "Tue, 3 Jun 2008 11:05:30 GMT"),
  ISO_LOCAL_DATE_TIME(DateTimeFormatter.ISO_LOCAL_DATE_TIME, "2011-12-03T10:15:30"),
  ISO_OFFSET_DATE_TIME(DateTimeFormatter.ISO_OFFSET_DATE_TIME, "2011-12-03T10:15:30+01:00");

  private final DateTimeFormatter value;
  private final String pattern;

  DateFormatSettings(DateTimeFormatter value, String pattern) {
    this.value = value;
    this.pattern = pattern;
  }

  public DateTimeFormatter getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.format("%s (%s)", name(), pattern);
  }
}
