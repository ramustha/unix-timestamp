package com.ramusthastudio.plugin.unixtimestamp.utils;

import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.codeInsight.hints.presentation.PresentationFactory;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.ramusthastudio.plugin.unixtimestamp.settings.AppSettingsState;
import junit.framework.TestCase;
import org.junit.Assert;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HelperTest extends TestCase {

  public void testIsMillisOrSecondsFormat() {
    Assert.assertTrue(Helper.isMillisOrSecondsFormat("1670587272776"));
    Assert.assertTrue(Helper.isMillisOrSecondsFormat("1607446800000"));
    Assert.assertFalse(Helper.isMillisOrSecondsFormat("1670587"));
  }

  public void testCreateInstantFormat() {
    Instant instantFormatSeconds = Helper.createInstantFormat("1607446800");
    Instant instantFormatMillis = Helper.createInstantFormat("1607446800000");

    Assert.assertEquals(Instant.ofEpochSecond(1607446800), instantFormatSeconds);
    Assert.assertEquals(Instant.ofEpochMilli(1607446800000L), instantFormatMillis);
  }

  public void testFindUnixTimestamp() {
    List<String> instantFormatSeconds = Helper.findUnixTimestamp("1607446800");
    List<String> instantFormatMillis = Helper.findUnixTimestamp("1607446800000");

    Assert.assertEquals(List.of("1607446800"), instantFormatSeconds);
    Assert.assertEquals(List.of("1607446800000"), instantFormatMillis);
  }

  public void testFindTextRange() {
    List<TextRange> instantFormatSeconds = Helper.findTextRange("1607446800", List.of("1607446800", "1607446800"));
    List<TextRange> instantFormatMillis = Helper.findTextRange("1607446800000", List.of("1607446800000", "1607446800000"));
    List<TextRange> instantFormatInvalid = Helper.findTextRange("1607446800000", List.of("16074468000001607446800000"));

    Assert.assertEquals(2, instantFormatSeconds.size());
    Assert.assertEquals(2, instantFormatMillis.size());
    Assert.assertEquals(0, instantFormatInvalid.size());
    Assert.assertEquals(10, instantFormatSeconds.get(0).getLength());
    Assert.assertEquals(13, instantFormatMillis.get(0).getLength());
  }

  public void testTestFindTextRange() {
    List<TextRange> instantFormatSeconds = Helper.findTextRange("1607446800", "1607446800, 1607446800000");
    List<TextRange> instantFormatMillis = Helper.findTextRange("1607446800000", "1607446800000, 1607446800");

    Assert.assertEquals(0, instantFormatSeconds.size());
    Assert.assertEquals(0, instantFormatMillis.size());
  }

  public void testCreateInlayHintsElement() {
    PsiElement psiElement = mock(PsiElement.class);
    InlayHintsSink inlayHintsSink = mock(InlayHintsSink.class);
    PresentationFactory presentationFactory = mock(PresentationFactory.class);
    AppSettingsState appSettingsState = mock(AppSettingsState.class);

    when(psiElement.getText()).thenReturn("1607446800000");
    when(appSettingsState.getDefaultLocalFormatter()).thenReturn(DateTimeFormatter.ISO_INSTANT);

    Helper.createInlayHintsElement(psiElement, inlayHintsSink, presentationFactory, appSettingsState);
  }
}
