package com.izamha.shakisha.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navigation

import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import com.izamha.shakisha.ui.components.JetsnackScaffold
import com.izamha.shakisha.ui.components.JetsnackSnackbar
import com.izamha.shakisha.ui.home.HomeSections
import com.izamha.shakisha.ui.home.ShakishaBottomBar
import com.izamha.shakisha.ui.home.addHomeGraph
import com.izamha.shakisha.ui.snackdetail.SnackDetail
import com.izamha.shakisha.ui.theme.ShakishaTheme

@Composable
fun JetsnackApp() {
    ProvideWindowInsets {
        ShakishaTheme {
            val appStateHolder = rememberAppStateHolder()
            JetsnackScaffold(
                bottomBar = {
                    if (appStateHolder.shouldShowBottomBar) {
                        ShakishaBottomBar(
                            tabs = appStateHolder.bottomBarTabs,
                            currentRoute = appStateHolder.currentRoute!!,
                            navigateToRoute = appStateHolder::navigateToBottomBarRoute
                        )
                    }
                },
                snackbarHost = {
                    SnackbarHost(
                        hostState = it,
                        modifier = Modifier.systemBarsPadding(),
                        snackbar = { snackbarData -> JetsnackSnackbar(snackbarData) }
                    )
                },
                scaffoldState = appStateHolder.scaffoldState
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appStateHolder.navController,
                    startDestination = MainDestinations.HOME_ROUTE,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    jetsnackNavGraph(
                        onSnackSelected = appStateHolder::navigateToSnackDetail,
                        upPress = appStateHolder::upPress
                    )
                }
            }
        }
    }
}

private fun NavGraphBuilder.jetsnackNavGraph(
    onSnackSelected: (Long, NavBackStackEntry) -> Unit,
    upPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(onSnackSelected)
    }
    composable(
        "${MainDestinations.SNACK_DETAIL_ROUTE}/{${MainDestinations.SNACK_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.SNACK_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val snackId = arguments.getLong(MainDestinations.SNACK_ID_KEY)
        SnackDetail(snackId, upPress)
    }
}
