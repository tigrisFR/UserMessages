package fr.nabonne.usermessages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import fr.nabonne.usermessages.data.localstorage.LocalStoreImpl
import fr.nabonne.usermessages.data.network.RemoteApiImpl
import fr.nabonne.usermessages.domain.LocalStore
import fr.nabonne.usermessages.domain.RemoteApi
import fr.nabonne.usermessages.ui.theme.UserMessagesTheme

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
                    AppNavHost()
                }
            }
        }
    }
}
