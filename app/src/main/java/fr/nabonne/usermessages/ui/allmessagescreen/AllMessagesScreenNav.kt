package fr.nabonne.usermessages.ui.allmessagescreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

//need public visibility for startDestination
const val ROUTE = "allMessages"

fun NavController.navigateToAllMessages(
    navOptions: NavOptions? = null,
) {
    this.navigate("$ROUTE", navOptions)
}

fun NavGraphBuilder.allMessagesScreen(
    onUserNavigationCb: (user: String) -> Unit,
    onComposerNavigationCb: (user: String?) -> Unit,
) {
    composable("$ROUTE") {
        AllMessagesScreen(
            onUserNavigationCb = onUserNavigationCb,
            onComposerNavigationCb = onComposerNavigationCb,
        )
    }
}