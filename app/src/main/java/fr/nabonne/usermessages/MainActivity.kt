package fr.nabonne.usermessages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import fr.nabonne.usermessages.data.localstorage.LocalStoreImpl
import fr.nabonne.usermessages.data.network.RemoteApiImpl
import fr.nabonne.usermessages.domain.LocalStore
import fr.nabonne.usermessages.domain.RemoteApi
import fr.nabonne.usermessages.domain.model.Message
import fr.nabonne.usermessages.ui.allmessagescreen.AllMessagesScreen
import fr.nabonne.usermessages.ui.theme.UserMessagesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {
    //TODO: proper Dependency injection
    companion object {
        val remoteApi: RemoteApi by lazy { RemoteApiImpl() }
        val localStore: LocalStore by lazy { LocalStoreImpl() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UserMessagesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //TODO properly bring these in a Usecase and a viewModel
                    val coroutineScope = rememberCoroutineScope()
                    val postCb: () -> Unit = remember {
                        {
                            coroutineScope.launch(Dispatchers.IO) {
                                val messageUniqueContent = UUID.randomUUID()
                                remoteApi.postMessage(
                                    message = Message(
                                        author = "dan",
                                        subject = "pets",
                                        content = "cats are grumpy $messageUniqueContent"
                                    )
                                )
                            }
                        }
                    }
                    Scaffold(
                        floatingActionButton = {
                            LargeFloatingActionButton(
                                onClick = postCb,
                                shape = CircleShape,) {
                                Icon(Icons.Default.Create, contentDescription = "Add")
                            }
                        },
                    ) { innerPadding ->
                        AllMessagesScreen(modifier = Modifier
                            .padding(innerPadding))
                    }
                }
            }
        }
    }
}
