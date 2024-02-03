package fr.nabonne.usermessages.ui.messagecomposerscreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private const val ROUTE_PREFIX = "composer"

fun NavController.navigateToComposer(
    user: String? = null,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        route = "$ROUTE_PREFIX?$USER_ID_ARG=${user?.trim()}",
        navOptions = navOptions,
    )
}

private const val USER_ID_ARG = "user"
fun NavGraphBuilder.messageComposerScreen(
    onMessageSentNavigationCb: () -> Unit
) {
    composable("$ROUTE_PREFIX?$USER_ID_ARG={$USER_ID_ARG}",
        arguments = listOf(navArgument("user") {
            nullable = true
        })
    ) {
        MessageComposerScreen(
            userProp = it.arguments?.getString(USER_ID_ARG),
            onMessageSentNavigationCb = onMessageSentNavigationCb,
        )
    }
}