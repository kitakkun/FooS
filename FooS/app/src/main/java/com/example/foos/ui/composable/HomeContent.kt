package com.example.foos.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foos.Posts
import com.example.foos.R
import com.example.foos.ui.composable.component.UserIcon

@Composable
fun HomeContent(navController: NavController) {
    val postList = Posts.getPosts()
    LazyColumn {
        items(postList) { post ->
            Post(username = post.username, content = post.content, location = post.location)
        }
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
fun Post(
    icon: Int = R.drawable.ic_launcher_foreground,
    username: String = "Username",
    content: String = "dummytext",
    location: String = "at xxxxx",
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 3.dp,
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(25)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                UserIcon(icon)
                Spacer(modifier = Modifier.width(20.dp))
                Column() {
                    Text(username)
                    Text(content, textAlign = TextAlign.Justify)
                    Text(location, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Right)
                }
            }
            ReactionRow(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun ReactionRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        FavoriteButton()
        FavoriteButton()
    }

}

@Composable
fun FavoriteButton() {
    var liked by remember { mutableStateOf(false) }
    Image(
        painter = painterResource(if (liked) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
        contentDescription = null,
        modifier = Modifier.clickable {
            liked = !liked
        }
    )
}


