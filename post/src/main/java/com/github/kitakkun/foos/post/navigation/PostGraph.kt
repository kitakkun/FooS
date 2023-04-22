package com.github.kitakkun.foos.post.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.kitakkun.foos.common.ext.composable
import com.github.kitakkun.foos.common.navigation.PostScreenRouter
import com.github.kitakkun.foos.common.navigation.StringList
import com.github.kitakkun.foos.post.create.PostCreateViewModel
import com.github.kitakkun.foos.post.create.edit.PostEditScreen
import com.github.kitakkun.foos.post.create.locationconfirm.LocationConfirmScreen
import com.github.kitakkun.foos.post.create.locationselect.LocationSelectScreen
import com.github.kitakkun.foos.post.imagedetail.ImageDetailScreen
import com.github.kitakkun.foos.post.postdetail.PostDetailScreen
import com.github.kitakkun.foos.post.postdetail.PostDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.inject

fun NavGraphBuilder.postGraph(navController: NavController) {
    postCreateGraph(navController)
    postDetailGraph(navController)
    imageDetailGraph(navController)
}

private fun NavGraphBuilder.postCreateGraph(navController: NavController) {
    navigation(
        route = PostScreenRouter.PostCreate.route,
        startDestination = PostScreenRouter.PostCreate.Edit.route,
    ) {
        val viewModel: PostCreateViewModel by inject(PostCreateViewModel::class.java)
        composable(PostScreenRouter.PostCreate.Edit.route) {
            PostEditScreen(
                viewModel = viewModel,
                navController = navController,
            )
        }
        composable(PostScreenRouter.PostCreate.LocationSelect) {
            LocationSelectScreen(
                viewModel = viewModel,
                navController = navController,
            )
        }
        composable(PostScreenRouter.PostCreate.LocationConfirm) {
            LocationConfirmScreen(
                viewModel = viewModel, navController = navController
            )
        }
    }
}

private fun NavGraphBuilder.postDetailGraph(navController: NavController) {
    composable(PostScreenRouter.Detail.PostDetail) {
        val args = PostScreenRouter.Detail.PostDetail.resolveArguments(it)
        val postId = args[0] as String
        val vm: PostDetailViewModel = koinViewModel()
        PostDetailScreen(vm, navController, postId)
    }
}

private fun NavGraphBuilder.imageDetailGraph(navController: NavController) {
    composable(PostScreenRouter.Detail.ImageDetail) {
        val args = PostScreenRouter.Detail.ImageDetail.resolveArguments(it)
        val imageUrls = args[0] as StringList
        val clickedImageIndex = args[1] as Int
        ImageDetailScreen(
            navController = navController,
            imageUrls = imageUrls.value,
            initialIndex = clickedImageIndex,
        )
    }
}
