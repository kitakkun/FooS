package com.github.kitakkun.foos.common.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class BottomSheetRouter(
    val route: String,
    val arguments: List<NamedNavArgument> = listOf()
) {

    /**
     * ナビゲーション引数のキーを取得
     */
    fun key(index: Int): String {
        return this.arguments[index].name
    }

    /**
     * パラメータを考慮したナビゲーションルートを作成
     */
    val routeWithArgs: String
        get() = route + arguments.map { it.name }.joinToString(separator = "") { "/{$it}" }

    /**
     * パラメータを代入してナビゲーションルートを作成
     */
    fun route(vararg params: String): String {
        if (params.toList().size != arguments.size) {
            throw Exception("Illegal arguments length for navigation params.")
        }
        return route + params.toList().joinToString(separator = "") { "/$it" }
    }

    object PostOption : BottomSheetRouter(
        route = "post_option",
        arguments = listOf(
            navArgument("postId") {
                type = NavType.StringType
            }
        )
    )
}
