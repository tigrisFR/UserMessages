package fr.nabonne.usermessages.ui.usermessagesscreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable


private const val ROUTE_PREFIX = "userMessages"

fun NavController.navigateToUserMessages(
    user: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        route = "$ROUTE_PREFIX/${user?.trim()}",
        navOptions = navOptions,
    )
}

private const val USER_ID_ARG = "user"
fun NavGraphBuilder.userMessagesScreen(
    onComposerNavigationCb: (user: String?) -> Unit
) {
    composable("$ROUTE_PREFIX/{$USER_ID_ARG}") {
        val user = it.arguments?.getString(USER_ID_ARG)
            ?: throw IllegalArgumentException("navigation to UserMessages screen without a user")
        UserMessagesScreen(userProp = user, onComposerNavigationCb = onComposerNavigationCb)
    }
}