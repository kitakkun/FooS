package com.github.kitakkun.foos.common.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

class PostScreenRouter : ScreenRouter(route = "post") {

    object PostCreate : ScreenRouter("post_create") {
        object Edit : ScreenRouter("post_edit")

        object LocationSelect : ScreenRouter("location_select")

        object LocationConfirm : ScreenRouter("location_confirm")
    }

    object Detail : ScreenRouter(route = "detail") {
        object ImageDetail : ScreenRouter(
            route = "image_detail",
            arguments = listOf(
                navArgument("imageUrls") { type = StringListType },
                navArgument("clickedImageIndex") { type = NavType.StringType },
            )
        )

        object PostDetail : ScreenRouter(
            route = "post_detail",
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        )
    }
}
