package com.example.foos

object nav_graph {

    // NavGraphのID
    const val id = 1

    object dest {
        const val home = 2
        const val post = 3
        const val map = 4
        const val setting = 5
    }

    object action {
        const val to_post_detail = 6
        const val to_image_detail = 7
    }

    object args {
        const val imageUris = "imageUrisId"
    }
}