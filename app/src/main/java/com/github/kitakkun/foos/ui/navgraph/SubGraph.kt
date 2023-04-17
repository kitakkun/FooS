package com.github.kitakkun.foos.ui.navgraph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.kitakkun.foos.common.ScreenViewModel
import com.github.kitakkun.foos.common.navigation.StringList
import com.github.kitakkun.foos.common.navigation.SubScreen
import com.github.kitakkun.foos.post.create.postCreateGraph
import com.github.kitakkun.foos.post.imagedetail.ImageDetailScreen
import com.github.kitakkun.foos.post.postdetail.PostDetailScreen
import com.github.kitakkun.foos.post.postdetail.PostDetailViewModelImpl

fun NavGraphBuilder.subGraph(
    navController: NavController,
    screenViewModel: ScreenViewModel,
) {
    postCreateGraph(navController, screenViewModel)
    composable(
        SubScreen.PostDetail.routeWithParam,
        SubScreen.PostDetail.arguments,
    ) {
        val postId = it.arguments?.getString(SubScreen.PostDetail.key(0))
        postId?.let {
            val vm: PostDetailViewModelImpl = hiltViewModel()
            PostDetailScreen(vm, navController, postId)
        }
    }
    composable(
        SubScreen.ImageDetail.routeWithParam,
        SubScreen.ImageDetail.arguments,
    ) {
        val imageUrls =
            it.arguments?.getParcelable<StringList>(SubScreen.ImageDetail.key(0))
        val clickedImageIndex = it.arguments?.getString(SubScreen.ImageDetail.key(1))
        if (imageUrls != null && clickedImageIndex != null) {
            ImageDetailScreen(
                navController = navController,
                imageUrls = imageUrls.value,
                initialIndex = clickedImageIndex.toInt(),
            )
        }
    }
}
