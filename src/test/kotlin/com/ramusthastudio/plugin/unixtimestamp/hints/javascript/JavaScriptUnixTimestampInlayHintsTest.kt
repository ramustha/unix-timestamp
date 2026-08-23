package com.ramusthastudio.plugin.unixtimestamp.hints.javascript

import com.ramusthastudio.plugin.unixtimestamp.hints.UnixTimestampInlayHintsTestBase

class JavaScriptUnixTimestampInlayHintsTest : UnixTimestampInlayHintsTestBase() {
    fun testTimestampHintIsRendered() {
        assertTimestampHintRendered("timestamp.js", JavaScriptUnixTimestampInlayHints())
    }
}
