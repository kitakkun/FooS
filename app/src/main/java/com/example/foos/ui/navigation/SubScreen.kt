package com.example.foos.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.foos.ui.navigation.navargs.StringListType

/**
 * サブスクリーン（メインメニューのスクリーンから呼ばれる）
 * @param route ベースとなるナビゲーションルート
 * @param arguments 引数となるパラメータのNamedNavArgumentのリスト
 */
sealed class SubScreen(
    val route: String,
    val arguments: List<NamedNavArgument> = listOf(),
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
    val routeWithParam: String
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

    /**
     * 投稿詳細画面
     */
    object PostDetail : SubScreen(
        route = "post_detail",
        arguments = listOf(navArgument("postId") { type = NavType.StringType })
    )

    /**
     * 画像プレビュー画面
     */
    object ImageDetail : SubScreen(
        route = "image_detail",
        arguments = listOf(
            navArgument("imageUrls") { type = StringListType },
            navArgument("clickedImageIndex") { type = NavType.StringType }
        )
    )

    /**
     * 投稿作成画面
     */
    object PostCreate : SubScreen("post_create") {
        /**
         * 位置情報選択画面
         */
        object LocationSelect : SubScreen("location_select")

        /**
         * 位置情報確認画面
         */
        object LocationConfirm : SubScreen("location_confirm")
    }

    /**
     * ユーザプロフィール画面
     */
    object UserProfile : SubScreen(
        route = "user_profile",
        arguments = listOf(navArgument("userId") { type = NavType.StringType })
    )

    /**
     * フォローリスト画面
     */
    object FollowList : SubScreen(
        route = "follow_list",
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType },
            navArgument("followees") { type = NavType.BoolType },
        )
    )


}
