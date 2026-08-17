package com.ramusthastudio.plugin.unixtimestamp.utils

import com.intellij.openapi.util.TextRange
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import java.time.Instant
import java.time.format.DateTimeFormatter

class HelperTest : StringSpec({

    "createInstantFormat should correctly convert UNIX timestamp" {
        // Epoch seconds
        Helper.createInstantFormat("1700723850") shouldBe Instant.parse("2023-11-23T07:17:30.000Z")
        // Epoch millis
        Helper.createInstantFormat("1700723850123") shouldBe Instant.parse("2023-11-23T07:17:30.123Z")
        // Epoch millis with suffix
        Helper.createInstantFormat("1700723850123L") shouldBe Instant.parse("2023-11-23T07:17:30.123Z")
        // Epoch micros
        Helper.createInstantFormat("1732184141128000") shouldBe Instant.parse("2024-11-21T10:15:41.128Z")
        // Epoch nanos
        Helper.createInstantFormat("1732184198639000000") shouldBe Instant.parse("2024-11-21T10:16:38.639Z")
        // Epoch with decimal millis
        Helper.createInstantFormat("1700723850.123") shouldBe Instant.parse("2023-11-23T07:17:30Z")
        // Epoch with decimal micros
        Helper.createInstantFormat("1700723850.123456") shouldBe Instant.parse("2023-11-23T07:17:30Z")
        // Epoch with decimal nanos
        Helper.createInstantFormat("1700723850.123456789") shouldBe Instant.parse("2023-11-23T07:17:30Z")
    }

    "createInstantFormat should reject invalid and overflowing values without throwing" {
        Helper.createInstantFormat("").shouldBeNull()
        Helper.createInstantFormat("not-a-timestamp").shouldBeNull()
        Helper.createInstantFormat("9999999999999999999").shouldBeNull()
    }

    "createTimestamp should correctly convert formatted date-time string to UNIX timestamp" {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val value = "2033-05-18 03:33:20"
        val result = Helper.createTimestamp(value, formatter)
        result shouldBeGreaterThan 19999
    }

    "findUnixTimestamp should correctly identify unix timestamp in string" {
        val text = "This is an example string with UNIX timestamp 1609459200000."
        val result = Helper.findUnixTimestamp(text).toList()
        result[0] shouldBe Pair("1609459200000", TextRange(46, 59))
    }

    "Find all valid Unix timestamps in the text" {
        val text = "The unix times were 1479999999, 1479999999000 and there was also 1479999999"

        val result = Helper.findUnixTimestamp(text).map { pair -> pair.first }.toList()

        result.shouldContainAll(listOf("1479999999", "1479999999000"))
    }

    "Find Unix timestamps and ignore other numbers" {
        val text = "The unix times were 1479999999, 1479999999000 but there were also 12345, 789000"

        val result = Helper.findUnixTimestamp(text).map { pair -> pair.first }.toList()

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

        val result = Helper.findUnixTimestamp(largeString).map { it.first }.distinct().toList()

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

        val result = Helper.findUnixTimestamp(largeString).map { it.first }.distinct().toList()

        // Assert the size of the result
        result shouldHaveSize 3

        // Assert that the correct timestamps were found
        result[0] shouldBe timestamp1.toString()
        result[1] shouldBe timestamp2
        result[2] shouldBe timestamp3
    }

    "findUnixTimestamp should return the correct unique UNIX timestamps containing decimals" {
        val timestamp1 = "1700723850.123"
        val timestamp2 = "1688849864.123456"
        val timestamp3 = "1700723877.123456789"

        val largeString = buildString {
            append("First: $timestamp1. ")
            append("Second: $timestamp2. ")
            append("Third: $timestamp3. ")
        }

        val result = Helper.findUnixTimestamp(largeString).map { pair -> pair.first }.toList()

        result shouldHaveSize 3
        result[0] shouldBe timestamp1
        result[1] shouldBe timestamp2
        result[2] shouldBe timestamp3
    }

    "findUnixTimestamp should respect microsecond and nanosecond switches" {
        val text = "seconds=1700723850 micros=1732184141128000 nanos=1732184198639000000"

        Helper.findUnixTimestamp(
            text,
            isSupportMicroSeconds = false,
            isSupportNanoSeconds = false
        ).map { it.first }.toList() shouldBe listOf("1700723850")
    }

    "findUnixTimestamp should reject unsupported timestamp lengths" {
        val text = "valid=1700723850 invalid=17007238501 invalid=170072385012"

        Helper.findUnixTimestamp(text).map { it.first }.toList() shouldBe listOf("1700723850")
    }

    "findUnixTimestamp should accept a CharSequence and stop at the match limit" {
        val text = buildString {
            repeat(50_000) { index ->
                append(1_700_000_000_000L + index).append(' ')
            }
        }

        val result = Helper.findUnixTimestamp(StringBuilder(text), maxMatches = 1_000).toList()

        result shouldHaveSize 1_000
        result.first().first shouldBe "1700000000000"
        result.last().first shouldBe "1700000000999"
    }

})
