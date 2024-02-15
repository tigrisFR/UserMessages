package fr.nabonne.usermessages.features.composer

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.nabonne.usermessages.common.domain.model.Message
import fr.nabonne.usermessages.common.ui.theme.UserMessagesTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun MessageComposerScreen(
    screenViewModel: MessageComposerScreenViewModel,
    modifier: Modifier = Modifier,
    userProp: String?,
    onMessageSentNavigationCb: () -> Unit,
) {
    val state by screenViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        if (state is MessageComposerScreenViewModel.UiState.SENT)
            onMessageSentNavigationCb.invoke()
    }
    MessageComposerScreen(
        modifier = modifier,
        userProp = userProp,
        state = state,
        viewModelInputs = screenViewModel.uiInputs,
    )
}


@Composable
internal fun MessageComposerScreen(
    modifier: Modifier = Modifier.fillMaxWidth(),
    userProp: String?,
    state: MessageComposerScreenViewModel.UiState,
    viewModelInputs: MutableStateFlow<MessageComposerScreenViewModel.UiInput>,
) {

    var author by remember { mutableStateOf(userProp ?: "prefilled author") }
    var subject by remember { mutableStateOf("prefilled subject") }
    var content by remember {
        mutableStateOf(
            "prefilled content ${UUID.randomUUID()}"
        )
    }

    LaunchedEffect(author, subject, content) {
        if (isActive) {
            viewModelInputs.value = MessageComposerScreenViewModel.UiInput.UpdateDraft(
                Message(
                    author = author,
                    subject = subject,
                    content = content,
                )
            )
        }
    }
    val isReadyToSend = remember (state) {
        state is MessageComposerScreenViewModel.UiState.READY_TO_SEND
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
                            viewModelInputs.value =
                                MessageComposerScreenViewModel.UiInput.SendMessage(
                                    Message(
                                        author = author,
                                        subject = subject,
                                        content = content,
                                    )
                                )
                        }
                    }
                },
                // Modify the appearance to reflect disabled state
                containerColor = if (isReadyToSend) MaterialTheme.colorScheme.primary else Color.Gray,
                // Optional: Adjust content color to match the enabled/disabled state
                contentColor = if (isReadyToSend) contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary) else Color.LightGray,
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
                state = MessageComposerScreenViewModel.UiState.READY_TO_SEND,
                userProp = null,
                viewModelInputs = MutableStateFlow(
                    MessageComposerScreenViewModel.UiInput.UpdateDraft(
                        Message("Dan", "ducks", "quack")
                    )
                ),
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Preview(showBackground = true)
@Composable
fun ScreenByAuthorPreviewFabDisabled() {
    UserMessagesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            MessageComposerScreen(
                state = MessageComposerScreenViewModel.UiState.NOT_READY_TO_SEND,
                userProp = null,
                viewModelInputs = MutableStateFlow(
                    MessageComposerScreenViewModel.UiInput.UpdateDraft(
                        Message("", "", "")
                    )
                ),
            )
        }
    }
}