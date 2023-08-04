package com.ramusthastudio.plugin.unixtimestamp.utils

import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.time.format.DateTimeFormatter

class HelperTestAI : StringSpec({
    "isMillisOrSecondsFormat should return true for valid MilliSeconds or Seconds formats" {
        Helper.isMillisOrSecondsFormat("1688848000000") shouldBe true  // valid millisecond timestamp
        Helper.isMillisOrSecondsFormat("1688848000") shouldBe true  // valid second timestamp
    }

    "isMillisOrSecondsFormat should return false for invalid MilliSeconds or Seconds formats" {
        Helper.isMillisOrSecondsFormat("168884800") shouldBe false // invalid timestamp
        Helper.isMillisOrSecondsFormat("InvalidValue") shouldBe false // invalid string
    }

    "createTimestamp should correctly convert formatted date-time string to UNIX timestamp" {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val value = "2033-05-18 03:33:20"
        val result = Helper.createTimestamp(value, formatter)
        result shouldBe 1999974800000L  // This should be the expected UNIX timestamp
    }

    "findUnixTimestamp should correctly identify unix timestamp in string" {
        val text = "This is an example string with UNIX timestamp 1609459200000."
        val result = Helper.findUnixTimestamp(text)
        result shouldHaveSize 1
        result[0] shouldBe "1609459200000"
    }

    "findUnixTimestamp should return the correct unique UNIX timestamps from a large string" {
        // Create a large string with sentences mixed with unix timestamps
        val timestamp1 = "1688849864454"
        val timestamp2 = "1688849864455"

        val largeString = buildString {
            repeat(5000) {
                append("This is a sample sentence timestamp: $timestamp1. ")
                append("This is another sample sentence timestamp: $timestamp2. ")
            }
        }

        val result = Helper.findUnixTimestamp(largeString)

        // Assert the size of the result
        result shouldHaveSize 2

        // Assert that the correct timestamps were found
        result[0] shouldBe timestamp1
        result[1] shouldBe timestamp2
    }
})
