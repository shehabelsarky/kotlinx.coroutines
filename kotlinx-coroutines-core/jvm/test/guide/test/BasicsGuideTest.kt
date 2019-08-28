/*
 * Copyright 2016-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

// This file was automatically generated from basics.md by Knit tool. Do not edit.
package kotlinx.coroutines.guide.test

import org.junit.Test

class BasicsGuideTest {

    @Test
    fun testKotlinxCoroutinesGuideBasic01() {
        test("KotlinxCoroutinesGuideBasic01") { kotlinx.coroutines.guide.basic01.main() }.verifyLines(
            "Hello,",
            "World!"
        )
    }

    @Test
    fun testKotlinxCoroutinesGuideBasic02() {
        test("KotlinxCoroutinesGuideBasic02") { kotlinx.coroutines.guide.basic02.main() }.verifyLines(
            "Hello,",
            "World!"
        )
    }

    @Test
    fun testKotlinxCoroutinesGuideBasic03() {
        test("KotlinxCoroutinesGuideBasic03") { kotlinx.coroutines.guide.basic03.main() }.verifyLines(
            "Hello,",
            "World!"
        )
    }

    @Test
    fun testKotlinxCoroutinesGuideBasic04() {
        test("KotlinxCoroutinesGuideBasic04") { kotlinx.coroutines.guide.basic04.main() }.verifyLines(
            "Hello,",
            "World!"
        )
    }

    @Test
    fun testKotlinxCoroutinesGuideBasic05() {
        test("KotlinxCoroutinesGuideBasic05") { kotlinx.coroutines.guide.basic05.main() }.verifyLines(
            "Hello,",
            "World!"
        )
    }

    @Test
    fun testKotlinxCoroutinesGuideBasic06() {
        test("KotlinxCoroutinesGuideBasic06") { kotlinx.coroutines.guide.basic06.main() }.verifyLines(
            "Task from coroutine scope",
            "Task from runBlocking",
            "Task from nested launch",
            "Coroutine scope is over"
        )
    }

    @Test
    fun testKotlinxCoroutinesGuideBasic07() {
        test("KotlinxCoroutinesGuideBasic07") { kotlinx.coroutines.guide.basic07.main() }.verifyLines(
            "Hello,",
            "World!"
        )
    }

    @Test
    fun testKotlinxCoroutinesGuideBasic08() {
        test("KotlinxCoroutinesGuideBasic08") { kotlinx.coroutines.guide.basic08.main() }.also { lines ->
            check(lines.size == 1 && lines[0] == ".".repeat(100_000))
        }
    }

    @Test
    fun testKotlinxCoroutinesGuideBasic09() {
        test("KotlinxCoroutinesGuideBasic09") { kotlinx.coroutines.guide.basic09.main() }.verifyLines(
            "I'm sleeping 0 ...",
            "I'm sleeping 1 ...",
            "I'm sleeping 2 ..."
        )
    }
}
