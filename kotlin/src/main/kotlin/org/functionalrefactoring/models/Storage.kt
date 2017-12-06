package org.functionalrefactoring.models

interface Storage<T> {
    fun flush(item: T)
}

