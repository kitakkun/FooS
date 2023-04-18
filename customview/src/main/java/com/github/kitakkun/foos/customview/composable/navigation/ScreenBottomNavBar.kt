package com.github.kitakkun.foos.customview.composable.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kitakkun.foos.common.navigation.ScreenRouter
import com.github.kitakkun.foos.customview.preview.PreviewContainer

@Composable
fun ScreenBottomNavBar(
    isVisible: Boolean,
    currentRoute: String?,
    onHomeClick: () -> Unit,
    onMapsClick: () -> Unit,
    onReactionsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        BottomNavigation(
            modifier = modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp
                    ),
                )
        ) {
            MyBottomNavigationItem(
                icon = Icons.Default.Home,
                label = stringResource(id = com.github.kitakkun.foos.common.R.string.home),
                isSelected = currentRoute == ScreenRouter.Main.Home.route,
                onClick = onHomeClick,
            )
            MyBottomNavigationItem(
                icon = Icons.Default.PinDrop,
                label = stringResource(id = com.github.kitakkun.foos.common.R.string.map),
                isSelected = currentRoute == ScreenRouter.Main.Map.route,
                onClick = onMapsClick,
            )
            MyBottomNavigationItem(
                icon = Icons.Default.Favorite,
                label = stringResource(id = com.github.kitakkun.foos.common.R.string.reaction),
                isSelected = currentRoute == ScreenRouter.Main.Reaction.route,
                onClick = onReactionsClick,
            )
            MyBottomNavigationItem(
                icon = Icons.Default.Settings,
                label = stringResource(id = com.github.kitakkun.foos.common.R.string.setting),
                isSelected = currentRoute == ScreenRouter.Main.Setting.route,
                onClick = onSettingsClick,
            )
        }
    }
}

@Composable
private fun RowScope.MyBottomNavigationItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    BottomNavigationItem(
        label = {
            Text(text = label)
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        selected = isSelected,
        onClick = onClick,
    )
}

@Preview
@Composable
private fun ScreenBottomNavBarPreview() = PreviewContainer {
    var isVisible by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Toggle")
            Switch(checked = isVisible, onCheckedChange = { isVisible = it })
        }
        Spacer(modifier = Modifier.height(16.dp))
        ScreenBottomNavBar(
            isVisible = isVisible,
            currentRoute = ScreenRouter.Main.Home.route,
            onHomeClick = {},
            onMapsClick = {},
            onReactionsClick = {},
            onSettingsClick = {},
        )
    }
}
