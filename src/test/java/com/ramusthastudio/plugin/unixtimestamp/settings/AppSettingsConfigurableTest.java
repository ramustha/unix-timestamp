package com.ramusthastudio.plugin.unixtimestamp.settings;

import com.intellij.openapi.options.ConfigurationException;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Ignore;


public class AppSettingsConfigurableTest extends TestCase {

  @Ignore
  public void testAppSettingsConfigurable() throws ConfigurationException {
    AppSettingsConfigurable appSettingsConfigurable = new AppSettingsConfigurable();
    appSettingsConfigurable.createComponent();

    Assert.assertNotNull(appSettingsConfigurable.getDisplayName());
    Assert.assertNotNull(appSettingsConfigurable.getPreferredFocusedComponent());
  }

}
