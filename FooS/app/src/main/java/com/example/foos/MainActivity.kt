package com.example.foos

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperColors.fromBitmap
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foos.ui.theme.FooSTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.maps.android.compose.*
import com.google.maps.android.ui.IconGenerator
import io.grpc.inprocess.AnonymousInProcessSocketAddress


class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private val ANONYMOUS = "匿名"

    private fun getPhotoUrl() : String? {
        val user = auth.currentUser
        return user?.photoUrl?.toString()
    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else ANONYMOUS
    }

    private fun checkSignInState() {
        if (auth.currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
    }

    override fun onStart() {
        super.onStart()
        checkSignInState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        checkSignInState()
        // When running in debug mode, connect to the Firebase Emulator Suite.
        // "10.0.2.2" is a special IP address which allows the Android Emulator
        // to connect to "localhost" on the host computer. The port values (9xxx)
        // must match the values defined in the firebase.json file.
        if (BuildConfig.DEBUG) {
            Firebase.database.useEmulator("10.0.2.2", 9000)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.storage.useEmulator("10.0.2.2", 9199)
        }


        setContent {
            FooSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App()
                }
            }
        }
    }
}


sealed class Screen(val route: String, @StringRes val stringId: Int, @DrawableRes val iconId: Int) {
    object Home : Screen("home", R.string.home, R.drawable.ic_home)
    object Map : Screen("map", R.string.map, R.drawable.ic_map)
}

@Composable
fun App() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Map
    )
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.iconId),
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(id = screen.stringId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { Home(navController) }
            composable(Screen.Map.route) { Map(navController) }
        }
    }
}

@Composable
fun Home(navController: NavController) {
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
    content: String = "dummytextdummytextdummytextdummytextdummytextdummytextdummytextdummytextdummytextdummytextdummytextdummytextdummytextdummytextdummytextdummytextdummytext",
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
            Row(
            ) {

                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .width(50.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .border(1.dp, Color.Gray)
                )

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


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Map(navController: NavController) {

    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    when (locationPermissionState.status) {
        PermissionStatus.Granted -> {
            val properties by remember {
                mutableStateOf(MapProperties(isMyLocationEnabled = true))
            }
            val uiSettings by remember {
                mutableStateOf(MapUiSettings(myLocationButtonEnabled = true))
            }

            var latLng by remember { mutableStateOf(LatLng(1.35, 103.87)) }
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(latLng, 20f)
            }

            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)

            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    Log.d("LOCATION", location.toString())
                    latLng = LatLng(it.latitude, it.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 20f)
                }
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings,
            ) {
                val iconGenerator = IconGenerator(LocalContext.current)
                iconGenerator.setBackground(null)
                val textView = TextView(LocalContext.current)
                textView.text = "Hello"
                iconGenerator.setContentView(textView)

                Marker(
                    state = MarkerState(position = latLng),
                    icon = BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())
                )


            }

        }
        else -> {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Please give the permission."
                )
                Button(onClick = {locationPermissionState.launchPermissionRequest()} ) {
                    Text("Request permission")
                }
            }
        }
    }
}
