package com.github.kitakkun.foos.user

// 依存きれなかったのでとりあえず複製して配置
fun <T> List<List<T>>.join(): List<T> {
    val result = mutableListOf<T>()
    this.forEach {
        result.addAll(it)
    }
    return result
}
