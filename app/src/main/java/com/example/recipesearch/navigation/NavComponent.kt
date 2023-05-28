package com.example.recipesearch.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipesearch.repositories.MainRepositoryImpl
import com.example.recipesearch.ui.screens.HomeScreen
import com.example.recipesearch.ui.screens.RecipeScreen
import com.example.recipesearch.ui.screens.SavedRecipesScreen
import com.example.recipesearch.ui.screens.SearchScreen
import com.example.recipesearch.ui.viewmodels.SharedViewModel
import com.example.recipesearch.ui.viewmodels.SharedViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavComponent() {
    val burgerMenuState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel(
        factory = SharedViewModelFactory(
            repository = MainRepositoryImpl,
            context = LocalContext.current
        )
    )
    Scaffold(
        topBar = {
            MyTopBar(
                clearFocus = { focusManager.clearFocus() },
                toggleBurgerMenu = {
                    toggleBurgerMenu(
                        burgerMenuState = burgerMenuState,
                        scope = coroutineScope
                    )
                },
                navController = navController
            )
        }
    ) { innerPadding ->
        ModalNavigationDrawer(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            drawerState = burgerMenuState,
            scrimColor = Color.LightGray.copy(alpha = 0.5f),
            drawerContent = { BurgerMenu(navController = navController) },
            gesturesEnabled = true,
            content = {
                Column {
                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.Gray)
                    )
                    NavHost(
                        navController = navController,
                        startDestination = Route.HomeScreenRoute.route
                    ) {
                        composable(route = Route.HomeScreenRoute.route) {
                            closeBurgerMenu(
                                burgerMenuState = burgerMenuState,
                                scope = coroutineScope
                            )
                            HomeScreen(
                                viewModel = sharedViewModel,
                                navController = navController
                            )
                        }
                        composable(
                            route = Route.SearchScreenRoute.route + "/{query}",
                            arguments = listOf(
                                navArgument("query") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            )
                        ) { backStackEntry ->
                            closeBurgerMenu(
                                burgerMenuState = burgerMenuState,
                                scope = coroutineScope
                            )
                            SearchScreen(
                                viewModel = sharedViewModel,
                                navController = navController,
                                query = backStackEntry.arguments!!.getString("query")!!
                            )
                        }
                        composable(route = Route.RecipeScreenRoute.route) {
                            closeBurgerMenu(
                                burgerMenuState = burgerMenuState,
                                scope = coroutineScope
                            )
                            RecipeScreen(
                                viewModel = sharedViewModel,
                                navController = navController
                            )
                        }
                        composable(route = Route.SavedRecipesScreenRoute.route) {
                            closeBurgerMenu(
                                burgerMenuState = burgerMenuState,
                                scope = coroutineScope
                            )
                            SavedRecipesScreen(
                                viewModel = sharedViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        )
    }
}

private fun getAppBarTitle(currentRoute: String?): String {
    val resId = when (currentRoute?.takeWhile { it.isLetter() }) {
        Route.SearchScreenRoute.route -> Route.SearchScreenRoute.title
        Route.RecipeScreenRoute.route -> Route.RecipeScreenRoute.title
        Route.SavedRecipesScreenRoute.route -> Route.SavedRecipesScreenRoute.title
        // currentRoute may also be null on home when first loading the app
        else -> Route.HomeScreenRoute.title
    }
    return resId
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    clearFocus: () -> Unit,
    toggleBurgerMenu: () -> Unit,
    navController: NavController
) {
    val clearFocusModifier = Modifier.pointerInput(Unit) {
        detectTapGestures { clearFocus() }
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isNotOnBurgerPage = when (currentRoute) {
        null,
        Route.HomeScreenRoute.route,
        Route.SavedRecipesScreenRoute.route -> false
        else -> true
    }
    TopAppBar(
        modifier = clearFocusModifier,
        title = {
            Text(
                text = getAppBarTitle(currentRoute),
                color = Color.DarkGray
            )
        },
        navigationIcon = {
            if (isNotOnBurgerPage) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = Color.DarkGray,
                        contentDescription = "Back"
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        clearFocus()
                        toggleBurgerMenu()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = Icons.Default.Menu,
                        tint = Color.DarkGray,
                        contentDescription = "Burger menu"
                    )
                }
            }
        }
    )
}

@Composable
fun BurgerMenu(navController: NavController) {
    Row (
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        val menuWidth = 199.dp
        Column(
            modifier = Modifier
                .width(menuWidth)
                .fillMaxHeight()
        ) {
            Spacer(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(menuWidth.times(0.95f))
                    .height(1.dp)
                    .background(Color.Gray)
            )
            BurgerMenuItem(
                onClick = { navController.navigate(Route.HomeScreenRoute.route) },
                icon = Icons.Default.Search,
                text = "Search"
            )
            Spacer(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(menuWidth.times(0.95f))
                    .height(1.dp)
                    .background(Color.Gray)
            )
            BurgerMenuItem(
                onClick = { navController.navigate(Route.SavedRecipesScreenRoute.route) },
                icon = Icons.Default.FavoriteBorder,
                text = "Saved Recipes"
            )
            Spacer(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(menuWidth.times(0.95f))
                    .height(1.dp)
                    .background(Color.Gray)
            )
            BurgerMenuItem(
                onClick = {  },
                icon = Icons.Outlined.Settings,
                text = "Settings"
            )
            Spacer(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(menuWidth.times(0.95f))
                    .height(1.dp)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.weight(1f))

            Spacer(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(menuWidth.times(0.95f))
                    .height(1.dp)
                    .background(Color.Gray)
            )
            BurgerMenuItem(
                onClick = {  },
                icon = Icons.Outlined.Email,
                text = "Give Feedback"
            )
        }
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color.Gray)
        )
    }
}

@Composable
fun BurgerMenuItem(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = icon,
                tint = Color.DarkGray,
                contentDescription = text)
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                text = text
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun toggleBurgerMenu(
    burgerMenuState: DrawerState,
    scope: CoroutineScope
) {
    scope.launch {
        when (burgerMenuState.currentValue) {
            DrawerValue.Closed -> burgerMenuState.open()
            else -> burgerMenuState.close()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun closeBurgerMenu(
    burgerMenuState: DrawerState,
    scope: CoroutineScope
) {
    scope.launch {
        burgerMenuState.close()
    }
}