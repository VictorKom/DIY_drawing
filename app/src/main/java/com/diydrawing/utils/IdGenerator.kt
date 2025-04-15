package com.diydrawing.utils

import java.util.concurrent.atomic.AtomicInteger

class IdGenerator {
    private val counter = AtomicInteger(0)

    fun generate(): Int {
        return counter.getAndIncrement()
    }
}