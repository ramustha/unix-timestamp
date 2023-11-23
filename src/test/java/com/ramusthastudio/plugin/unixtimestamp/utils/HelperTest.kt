package com.ramusthastudio.plugin.unixtimestamp.utils

import com.intellij.openapi.util.TextRange
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class HelperTest : StringSpec({

    "createTimestamp should correctly convert formatted date-time string to UNIX timestamp" {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val value = "2033-05-18 03:33:20"
        val result = Helper.createTimestamp(value, formatter)
        result shouldBe 1999974800000L  // This should be the expected UNIX timestamp
    }

    "findUnixTimestamp should correctly identify unix timestamp in string" {
        val text = "This is an example string with UNIX timestamp 1609459200000."
        val result = Helper.findUnixTimestamp(text)
        result[0] shouldBe "1609459200000"
    }

    "Find all valid Unix timestamps in the text" {
        val text = "The unix times were 1479999999, 1479999999000 and there was also 1479999999"

        val result = Helper.findUnixTimestamp(text)

        result.shouldContainAll(listOf("1479999999", "1479999999000"))
    }

    "Find Unix timestamps and ignore other numbers" {
        val text = "The unix times were 1479999999, 1479999999000 but there were also 12345, 789000"

        val result = Helper.findUnixTimestamp(text)

        result.shouldContainAll(listOf("1479999999", "1479999999000"))
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

    "findUnixTimestamp should return the correct unique UNIX timestamps contains suffix l or L" {
        // Create a large string with sentences mixed with unix timestamps
        val timestamp1 = 1700723850083L
        val timestamp2 = "1688849864455l"
        val timestamp3 = "1700723877716L"

        val largeString = buildString {
            repeat(5000) {
                append("This is a sample sentence timestamp: $timestamp1. ")
                append("This is another sample sentence timestamp: $timestamp2. ")
                append("And This is another sample sentence timestamp: $timestamp3. ")
            }
        }

        val result = Helper.findUnixTimestamp(largeString)

        // Assert the size of the result
        result shouldHaveSize 3

        // Assert that the correct timestamps were found
        result[0] shouldBe timestamp1
        result[1] shouldBe timestamp2.dropLast(1)
        result[2] shouldBe timestamp3.dropLast(1)
    }

    "findTextRanges should return correct ranges when searching for a timestamp" {
        val timeStamp = "1691475292429"
        val text = "Hey there! Timestamp is here: 1691475292429. That's it."

        val result = Helper.findTextRanges(text, timeStamp)

        val expected = listOf(TextRange(30, 30 + timeStamp.length))

        result shouldBe expected
    }

    "findTextRanges should return empty list when targetWord not found" {
        val text = "Hello Universe"
        val targetWord = "World"

        val result =  Helper.findTextRanges(text, targetWord)

        result shouldBe emptyList()
    }

    "findTextRanges should work with huge strings" {
        // Generate a large string of random lowercase characters
        val hugeText = StringBuilder().apply {
            for (i in 1..1_000_000) {
                append(Random.nextInt(97, 122).toChar())
            }
        }.toString()

        // Insert the timestamp at 500_000 position
        val timeStamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        val hugeTextWithTarget = hugeText.substring(0, 500_000) + timeStamp + hugeText.substring(500_000)

        val result =  Helper.findTextRanges(hugeTextWithTarget, timeStamp)
        val expected = listOf(TextRange(500_000, 500_000 + timeStamp.length))

        result shouldBe expected
    }
})
