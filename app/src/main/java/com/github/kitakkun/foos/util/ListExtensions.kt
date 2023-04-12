package com.github.kitakkun.foos.util

fun <T> List<List<T>>.join(): List<T> {
    val result = mutableListOf<T>()
    this.forEach {
        result.addAll(it)
    }
    return result
}
