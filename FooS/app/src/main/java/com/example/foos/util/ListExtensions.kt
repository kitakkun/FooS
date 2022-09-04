package com.example.foos.util

fun <T> List<List<T>>.join(): List<T> {
    val result = mutableListOf<T>()
    this.forEach {
        result.addAll(it)
    }
    return result
}