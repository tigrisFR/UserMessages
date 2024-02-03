package fr.nabonne.usermessages.ui.messagecomposerscreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

private const val ROUTE_PREFIX = "composer"

fun NavController.navigateToComposer(
    user: String? = null,
    navOptions: NavOptions? = null,
) {
    this.navigate("$ROUTE_PREFIX/$user", navOptions)
}

private const val USER_ID_ARG = "user"
fun NavGraphBuilder.messageComposerScreen(
    onMessageSentNavigationCb: () -> Unit
) {
    composable("$ROUTE_PREFIX/{$USER_ID_ARG}") {
        var user = it.arguments?.getString(USER_ID_ARG)
        MessageComposerScreen(
            userProp = if (user == "null") null else user,
            onMessageSentNavigationCb = onMessageSentNavigationCb,
        )
    }
}