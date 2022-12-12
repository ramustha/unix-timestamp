package com.ramusthastudio.plugin.unixtimestamp.settings;

import junit.framework.TestCase;

public class AppSettingsStateTest extends TestCase {

  public void testGetInstance() {
    AppSettingsState appSettingsState = new AppSettingsState();

    assertNotNull(appSettingsState);
  }
}
