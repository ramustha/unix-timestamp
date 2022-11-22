package com.ramusthastudio.plugin.unixtimestamp;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

@Ignore
public class UnixTimestampInspectionTest extends LightJavaCodeInsightFixtureTestCase {

  @Override
  protected String getTestDataPath() {
    return "src/test/resources/testData";
  }

  protected void doTest(@NotNull String testName, int expectedHighlighting) {
    // Initialize the test based on the testData file
    myFixture.configureByFile(testName);
    // Initialize the inspection and get a list of highlighted
    myFixture.enableInspections(new UnixTimestampInspection());

    List<HighlightInfo> highlightInfos = myFixture.doHighlighting()
        .stream()
        .filter(h -> "UnixTimestamp".equalsIgnoreCase(h.getInspectionToolId()))
        .collect(Collectors.toList());
    assertSize(expectedHighlighting, highlightInfos);
  }

  @Test
  public void testUnknownFile() {
    doTest("sample-js.js", 3);
  }

  @Test
  public void testJavaFile() {
    doTest("sample-java.java", 4);
  }

  @Test
  public void testKotlinFile() {
    doTest("sample-kt.kt", 4);
  }

  @Test
  public void testJsonFile() {
    doTest("sample-json.json", 2);
  }

  @Test
  public void testHTMLFile() {
    doTest("sample-html.html", 1);
  }

  @Test
  public void testXHTMLFile() {
    doTest("sample-xhtml.xhtml", 1);
  }

  @Test
  public void testXMLFile() {
    doTest("sample-xml.xml", 1);
  }
}
