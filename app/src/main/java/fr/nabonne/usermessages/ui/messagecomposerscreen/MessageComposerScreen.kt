package fr.nabonne.usermessages.ui.messagecomposerscreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.nabonne.usermessages.MainActivity
import fr.nabonne.usermessages.domain.ComposeMessageUseCaseImpl
import fr.nabonne.usermessages.domain.model.Message
import fr.nabonne.usermessages.ui.theme.UserMessagesTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import java.util.Random
import java.util.UUID

@Composable
fun MessageComposerScreen(
    modifier: Modifier = Modifier,
    userProp: String?,
    onMessageSentNavigationCb: () -> Unit,
) {
    //TODO proper DI
    val viewModel: MessageComposerScreenViewModel = viewModel(
        initializer = {
            MessageComposerScreenViewModel(ComposeMessageUseCaseImpl(MainActivity.remoteApi))
        }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
//    LaunchedEffect(state) {
//        if (state is MessageComposerScreenViewModel.UiState.SENT)
//            onMessageSentNavigationCb.invoke()
//    }
    MessageComposerScreen(
        modifier = modifier,
        userProp = userProp,
        state = state,
        viewModelInputs = viewModel.uiInputs
    ) { message -> viewModel.postMessage(message) }
}


@Composable
internal fun MessageComposerScreen(
    modifier: Modifier = Modifier.fillMaxWidth(),
    userProp: String?,
    state: MessageComposerScreenViewModel.UiState,
    viewModelInputs: MutableStateFlow<Message>,
    postCb: (Message) -> Unit,
) {

//    val fakeAuthors = remember {
//        listOf("dan", "bob", "Hubert Bonisseur de La Bath")
//    }
    val messageUniqueContent = UUID.randomUUID()
//    val random = Random().nextInt()

//    var message by remember { mutableStateOf(Message(
//        author = "${fakeAuthors[random.mod(fakeAuthors.size)]}",
//        subject = "pets",
//        content = "cats are grumpy $messageUniqueContent"
//    )) }
    var message by remember { mutableStateOf(Message(
        author = userProp ?: "prefilled author",
        subject = "prefilled subject",
        content = "prefilled $messageUniqueContent",
    )) }
    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {
                    if (state !is MessageComposerScreenViewModel.UiState.READY_TO_SEND)
                        return@LargeFloatingActionButton
                    postCb(message)
                },
                containerColor = DarkGray,
                contentColor = Gray,
                shape = CircleShape,
            ) {
                Icon(Icons.Default.Create, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            OutlinedTextField(
                value = message.author,
                onValueChange = { message.copy(author = it) },
                label = { Text("Enter author name") }
            )
            OutlinedTextField(
                value = message.subject,
                onValueChange = { message.copy(subject = it) },
                label = { Text("Enter Subject") }
            )
            OutlinedTextField(
                modifier = modifier.fillMaxHeight(),
                value = message.content,
                onValueChange = { message.copy(content = it) },
                label = { Text("Enter Content") }
            )
        }
        LaunchedEffect(message) {
            while (isActive) {
                viewModelInputs.value = message
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Preview(showBackground = true)
@Composable
fun ScreenByAuthorPreview() {
    UserMessagesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            MessageComposerScreen(
                state = MessageComposerScreenViewModel.UiState.NOT_READY_TO_SEND,
                userProp = null,
                viewModelInputs = MutableStateFlow(Message("", "", "")),
                postCb = {}
            )
        }
    }
}