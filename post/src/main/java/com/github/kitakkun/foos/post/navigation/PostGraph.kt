package com.github.kitakkun.foos.post.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.kitakkun.foos.common.ScreenViewModel
import com.github.kitakkun.foos.common.ext.composable
import com.github.kitakkun.foos.common.navigation.PostScreenRouter
import com.github.kitakkun.foos.common.navigation.StringList
import com.github.kitakkun.foos.post.create.PostScreen
import com.github.kitakkun.foos.post.create.PostViewModelImpl
import com.github.kitakkun.foos.post.create.locationconfirm.LocationConfirmScreen
import com.github.kitakkun.foos.post.create.locationconfirm.LocationConfirmViewModelImpl
import com.github.kitakkun.foos.post.create.locationselect.LocationSelectScreen
import com.github.kitakkun.foos.post.create.locationselect.LocationSelectViewModelImpl
import com.github.kitakkun.foos.post.imagedetail.ImageDetailScreen
import com.github.kitakkun.foos.post.postdetail.PostDetailScreen
import com.github.kitakkun.foos.post.postdetail.PostDetailViewModelImpl

fun NavGraphBuilder.postGraph(
    navController: NavController,
    screenViewModel: ScreenViewModel,
) {
    postCreateGraph(navController, screenViewModel)
    postDetailGraph(navController)
    imageDetailGraph(navController)
}

private fun NavGraphBuilder.postCreateGraph(
    navController: NavController,
    screenViewModel: ScreenViewModel,
) {
    navigation(
        route = PostScreenRouter.PostCreate.route,
        startDestination = PostScreenRouter.PostCreate.Edit.route,
    ) {
        composable(PostScreenRouter.PostCreate.Edit.route) {
            val vm: PostViewModelImpl = hiltViewModel()
            PostScreen(vm, navController, screenViewModel)
        }
        composable(PostScreenRouter.PostCreate.LocationSelect) {
            val vm: LocationSelectViewModelImpl = hiltViewModel()
            LocationSelectScreen(vm, navController, screenViewModel)
        }
        composable(PostScreenRouter.PostCreate.LocationConfirm) {
            val vm: LocationConfirmViewModelImpl = hiltViewModel()
            LocationConfirmScreen(vm, navController, screenViewModel)
        }
    }
}

private fun NavGraphBuilder.postDetailGraph(navController: NavController) {
    composable(PostScreenRouter.Detail.PostDetail) {
        val args = PostScreenRouter.Detail.PostDetail.resolveArguments(it)
        val postId = args[0] as String
        val vm: PostDetailViewModelImpl = hiltViewModel()
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
