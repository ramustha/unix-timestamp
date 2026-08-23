package com.ramusthastudio.plugin.unixtimestamp.hints

class PlainTextUnixTimestampInlayHintsTest : UnixTimestampInlayHintsTestBase() {
    fun testTimestampHintIsRendered() {
        assertTimestampHintRendered("timestamp.txt", PlainTextUnixTimestampInlayHints())
    }
}
