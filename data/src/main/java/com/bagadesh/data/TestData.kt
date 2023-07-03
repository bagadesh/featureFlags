package com.bagadesh.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

/**
 * Created by bagadesh on 27/06/23.
 */
class TestData {

    suspend fun testing1() {
        println("Test")
    }

    suspend fun testing2(): Boolean {
        println("Test2")
        return Random.nextBoolean()
    }

    suspend fun testing3(): Boolean {
        println("Test3")
        val a = testing2()
        val b = testing2()
        return a && b
    }

    suspend fun testing4(): Boolean {
        return coroutineScope {
            println("Test4")
            val a = async { testing2() }
            val b = async { testing2() }
            a.await() && b.await()
        }

    }

}