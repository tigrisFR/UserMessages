package fr.nabonne.usermessages.ui.messagecomposerscreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.nabonne.usermessages.MainActivity
import fr.nabonne.usermessages.domain.ComposeMessageUseCaseImpl
import fr.nabonne.usermessages.domain.model.Message
import fr.nabonne.usermessages.ui.theme.UserMessagesTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun MessageComposerScreen(
    modifier: Modifier = Modifier,
    userProp: String?,
    onMessageSentNavigationCb: () -> Unit,
) {
    //TODO proper DI
    val screenViewModel: MessageComposerScreenViewModel = viewModel(
        initializer = {
            MessageComposerScreenViewModel(ComposeMessageUseCaseImpl(MainActivity.remoteApi))
        }
    )
    val state by screenViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        if (state is MessageComposerScreenViewModel.UiState.SENT)
            onMessageSentNavigationCb.invoke()
    }
    MessageComposerScreen(
        modifier = modifier,
        userProp = userProp,
        state = state,
//        viewModelInputs = viewModel.uiInputs,
        updateDraftCb = { screenViewModel.updateMessage(it)}
    ) { message -> screenViewModel.postMessage(message) }
}


@Composable
internal fun MessageComposerScreen(
    modifier: Modifier = Modifier.fillMaxWidth(),
    userProp: String?,
    state: MessageComposerScreenViewModel.UiState,
//    viewModelInputs: MutableStateFlow<Message>,
    updateDraftCb: (Message) -> Unit,
    postCb: (Message) -> Unit,
) {

    var author by remember { mutableStateOf("prefilled author") }
    var subject by remember { mutableStateOf("prefilled subject") }
    var content by remember {
        mutableStateOf(
            "prefilled content ${UUID.randomUUID()}"
        )
    }

    LaunchedEffect(author, subject, content) {
        while (isActive) {
            delay(500)
            updateDraftCb(
                Message(
                author = author,
                subject = subject,
                content = content,
                )
            )
//            viewModelInputs.value = message
        }
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {
                    scope.launch {
                        if (state !is MessageComposerScreenViewModel.UiState.READY_TO_SEND) {
                            snackbarHostState.showSnackbar("All fields must be filled!")
                        } else {
                            postCb(
                                Message(
                                    author = author,
                                    subject = subject,
                                    content = content,
                                )
                            )
                        }
                    }
                },
                shape = CircleShape,
            ) {
                Icon(Icons.Default.Create, contentDescription = "Compose")
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Enter author name") }
            )
            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Enter Subject") }
            )
            OutlinedTextField(
                modifier = modifier.fillMaxHeight(),
                value = content,
                onValueChange = { content = it },
                label = { Text("Enter Content") }
            )
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
//                viewModelInputs = MutableStateFlow(Message("", "", "")),
                updateDraftCb = {},
                postCb = {}
            )
        }
    }
}