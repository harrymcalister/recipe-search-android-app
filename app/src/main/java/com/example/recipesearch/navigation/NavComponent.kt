package com.example.recipesearch.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipesearch.repositories.MainRepositoryImpl
import com.example.recipesearch.ui.screens.HomeScreen
import com.example.recipesearch.ui.screens.RecipeScreen
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
            drawerContent = {
                Row (
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(200.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Column(
                        modifier = Modifier
                            .width(199.dp)
                            .fillMaxHeight()
                    ) {
                    }
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color.Gray)
                    )
                }
            },
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
                            SearchScreen(
                                viewModel = sharedViewModel,
                                navController = navController,
                                query = backStackEntry.arguments!!.getString("query")!!
                            )
                        }
                        composable(
                            route = Route.RecipeScreenRoute.route + "/{recipesIndex}",
                            arguments = listOf(
                                navArgument("recipesIndex") {
                                    type = NavType.IntType
                                    nullable = false
                                }
                            )
                        ) { backStackEntry ->
                            RecipeScreen(
                                viewModel = sharedViewModel,
                                navController = navController,
                                recipesIndex = backStackEntry.arguments!!.getInt("recipesIndex")
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
        null, Route.HomeScreenRoute.route -> Route.HomeScreenRoute.title
        Route.RecipeScreenRoute.route -> Route.RecipeScreenRoute.title
        else -> Route.SearchScreenRoute.title
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
    val isNotOnHomePage = when (currentRoute) {
        null, Route.HomeScreenRoute.route -> false
        else -> true
    }
    TopAppBar(
        modifier = clearFocusModifier,
        title = {
            Text(getAppBarTitle(currentRoute))
        },
        navigationIcon = {
            if (isNotOnHomePage) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
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
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Burger menu"
                    )
                }
            }
        }
    )
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