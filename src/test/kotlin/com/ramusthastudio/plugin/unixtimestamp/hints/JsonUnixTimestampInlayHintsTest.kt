package com.ramusthastudio.plugin.unixtimestamp.hints

class JsonUnixTimestampInlayHintsTest : UnixTimestampInlayHintsTestBase() {
    fun testTimestampHintIsRendered() {
        assertTimestampHintRendered("timestamp.json", JsonUnixTimestampInlayHints())
    }
}
