package fr.nabonne.usermessages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import fr.nabonne.usermessages.data.localstorage.LocalStoreImpl
import fr.nabonne.usermessages.data.network.RemoteApiImpl
import fr.nabonne.usermessages.domain.LocalStore
import fr.nabonne.usermessages.domain.RemoteApi
import fr.nabonne.usermessages.ui.allmessagescreen.allMessagesScreen
import fr.nabonne.usermessages.ui.messagecomposerscreen.messageComposerScreen
import fr.nabonne.usermessages.ui.messagecomposerscreen.navigateToComposer
import fr.nabonne.usermessages.ui.theme.UserMessagesTheme
import fr.nabonne.usermessages.ui.usermessagesscreen.navigateToUserMessages
import fr.nabonne.usermessages.ui.usermessagesscreen.userMessagesScreen

class MainActivity : ComponentActivity() {
    //TODO: proper Dependency injection
    companion object {
        val remoteApi: RemoteApi by lazy { RemoteApiImpl() }
        val localStore: LocalStore by lazy { LocalStoreImpl() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            UserMessagesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = fr.nabonne.usermessages.ui.allmessagescreen.ROUTE,
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
                }
            }
        }
    }
}
