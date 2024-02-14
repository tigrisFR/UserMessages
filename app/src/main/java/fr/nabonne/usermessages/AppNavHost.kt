package fr.nabonne.usermessages

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import fr.nabonne.usermessages.di.UserMessagesApp
import fr.nabonne.usermessages.di.UserMessagesApp.Companion.useCasesSubModule
import fr.nabonne.usermessages.features.composer.messageComposerScreen
import fr.nabonne.usermessages.features.composer.navigateToComposer
import fr.nabonne.usermessages.features.allmessages.allMessagesScreen
import fr.nabonne.usermessages.features.usermessages.navigateToUserMessages
import fr.nabonne.usermessages.features.usermessages.userMessagesScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = fr.nabonne.usermessages.features.allmessages.ROUTE,
) = NavHost(
    navController = navController,
    startDestination = startDestination,
) {
    allMessagesScreen(
        useCasesSubModule = UserMessagesApp.appModule.useCasesSubModule,
        onComposerNavigationCb = {
            navController.navigateToComposer(it)
        },
        onUserNavigationCb = {
            navController.navigateToUserMessages(it)
        }
    )
    userMessagesScreen(
        useCasesSubModule = UserMessagesApp.appModule.useCasesSubModule,
        onComposerNavigationCb = {
            navController.navigateToComposer(it)
        }
    )
    messageComposerScreen(
        useCasesSubModule = UserMessagesApp.appModule.useCasesSubModule,
        onMessageSentNavigationCb = {
            navController.popBackStack()
        },
    )
}