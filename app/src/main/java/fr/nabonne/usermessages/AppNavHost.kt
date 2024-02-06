package fr.nabonne.usermessages

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import fr.nabonne.usermessages.ui.allmessagescreen.allMessagesScreen
import fr.nabonne.usermessages.ui.messagecomposerscreen.messageComposerScreen
import fr.nabonne.usermessages.ui.messagecomposerscreen.navigateToComposer
import fr.nabonne.usermessages.ui.usermessagesscreen.navigateToUserMessages
import fr.nabonne.usermessages.ui.usermessagesscreen.userMessagesScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = fr.nabonne.usermessages.ui.allmessagescreen.ROUTE,
) = NavHost(
    navController = navController,
    startDestination = startDestination,
) {
    allMessagesScreen(
        onComposerNavigationCb = {
            navController.navigateToComposer(it)
        },
        onUserNavigationCb = {
            navController.navigateToUserMessages(it)
        }
    )
    userMessagesScreen(
        onComposerNavigationCb = {
            navController.navigateToComposer(it)
        }
    )
    messageComposerScreen(
        onMessageSentNavigationCb = {
            navController.popBackStack()
        }
    )
}