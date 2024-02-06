package fr.nabonne.usermessages.ui.allmessagescreen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.nabonne.usermessages.MainActivity
import fr.nabonne.usermessages.domain.GetAllMessagesUseCaseImpl
import fr.nabonne.usermessages.domain.model.Message
import fr.nabonne.usermessages.ui.theme.UserMessagesTheme


@Composable
fun AllMessagesScreen(
    modifier: Modifier = Modifier,
    onUserNavigationCb: (user: String) -> Unit,
    onComposerNavigationCb: (user: String?) -> Unit,
) {
    //TODO proper DI
    val allMessagesViewModel: AllMessagesScreenViewModel = viewModel(
        initializer = {
            AllMessagesScreenViewModel()
        }
    )

    val refreshCb: () -> Unit = remember {
        { allMessagesViewModel.refresh() }
    }
    val uiState by allMessagesViewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { onComposerNavigationCb(null) },
                shape = CircleShape,) {
                Icon(Icons.Default.Create, contentDescription = "Compose")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        AllMessagesScreen(
            modifier = modifier.padding(innerPadding),
            uiState = uiState,
            onUserNavigationCb = onUserNavigationCb,
            refreshCb = refreshCb,
        )
    }
}

@Composable
fun AllMessagesScreen(
    modifier: Modifier = Modifier,
    uiState: AllMessagesScreenViewModel.UiState,
    onUserNavigationCb: (user: String) -> Unit,
    refreshCb: () -> Unit,
) {
    Column(modifier = modifier){
        Button(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            onClick = refreshCb,
        ) {
            Text("Refresh All Messages")
        }
        when (uiState) {
            is AllMessagesScreenViewModel.UiState.ByAuthor -> {
                LazyColumnByAuthor(
                    modifier = modifier,
                    map = uiState.map,
                    onUserNavigationCb = onUserNavigationCb,
                )
            }
            is AllMessagesScreenViewModel.UiState.BySubject -> {
                LazyColumnBySubject(
                    modifier = modifier,
                    map = uiState.map,
                    onUserNavigationCb = onUserNavigationCb,
                )
            }
            is AllMessagesScreenViewModel.UiState.InOrder -> {
                LazyColumnInOrder(
                    modifier = modifier,
                    messages = uiState.list,
                    onUserNavigationCb = onUserNavigationCb,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnByAuthor(
    modifier: Modifier = Modifier.fillMaxWidth(),
    //TODO use snapshotMap
    map: Map<String, List<Message>>,
    onUserNavigationCb: (user: String) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        map.forEach {
            stickyHeader() {
                ElevatedButton(
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = { onUserNavigationCb(it.key) }
                ) {
                    Text(
                        text = it.key,
                    )
                }
            }
            items(it.value) { message ->
                MessageCard(message = message)
            }
        }
    }
}

@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    message: Message,
) {
    ElevatedCard(
        modifier = modifier
            .padding(start = 24.dp, bottom = 16.dp)
    )
    {
        Text(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            text = "Subject: ${message.subject}"
        )
        Text(
            modifier = modifier
                .padding(start = 24.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
            color = MaterialTheme.colorScheme.primary,
//                        textAlign = TextAlign.Justify,
            text = "${message.content}"
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LazyColumnBySubject(
    modifier: Modifier = Modifier.fillMaxWidth(),
    //TODO use snapshotMap
    map: Map<String, List<Message>>,
    onUserNavigationCb: (user: String) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        map.forEach {
            stickyHeader() {
                ElevatedButton(
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = { /*TODO screen by subject*/ }
                ) {
                    Text(
                        text = "Subject: ${it.key}",
                    )
                }
            }
            items(it.value) {
                ElevatedCard(
                    modifier = modifier
                        .padding(start = 24.dp, bottom = 16.dp),
                    onClick = { onUserNavigationCb(it.author) }
                )
                {
                    Text(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        text = "From: ${it.author}"
                    )
                    Text(
                        modifier = modifier
                            .padding(start = 24.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
                        color = MaterialTheme.colorScheme.primary,
//                        textAlign = TextAlign.Justify,
                        text = "${it.content}")
                }
            }
        }
    }
}

@Composable
fun LazyColumnInOrder(
    modifier: Modifier = Modifier.fillMaxWidth(),
    messages: List<Message>,
    onUserNavigationCb: (user: String) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(messages) {
            Column {
                ElevatedButton(
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = { onUserNavigationCb(it.author) }
                ) {
                    Text("${it.author}")
                }
                MessageCard(
                    modifier = modifier,
                    message = it,
                )
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
            color = MaterialTheme.colorScheme.background
        ) {
            AllMessagesScreen(
                uiState = AllMessagesScreenViewModel.UiState.ByAuthor(
                    mapOf(
                        "dan" to listOf(
                            Message("pets", "this is a test1", "dan"),
                            Message("pets", "this is a test2", "dan"),
                        ),
                        "bob" to listOf(
                            Message("pets",
                                """
                                    this is a test1
                                    this is a test2 This is a test2
                                    this is a test3 This is a test3 this is a test3 This is a test3
                                """.trimIndent(),
                                "bob"),
                            Message("boats", "this is a test2", "bob"),
                        ),
                        "lucy" to listOf(
                            Message("pets", "this is a test1", "lucy"),
                            Message("boats", "this is a test2", "lucy"),
                            Message("pets", "this is a test2", "lucy"),
                        ),
                        "Hubert Bonisseur de La Bath" to listOf(
                            Message("pets", "this is a test1", "Hubert Bonisseur de La Bath"),
                            Message("boats", "this is a test2", "Hubert Bonisseur de La Bath"),
                            Message("pets", "this is a test2", "Hubert Bonisseur de La Bath"),
                        )
                    ),
                    ""
                ),
                refreshCb = {},
                onUserNavigationCb = {},
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Preview(showBackground = true)
@Composable
fun ScreenBySubjectPreview() {
    UserMessagesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AllMessagesScreen(
                uiState = AllMessagesScreenViewModel.UiState.BySubject(
                    mapOf(
                        "pets" to listOf(
                            Message("pets", "this is a test1", "dan"),
                            Message("pets", "this is a test2", "dan"),
                        ),
                        "boats" to listOf(
                            Message("boats",
                                """
                                    this is a test1
                                    this is a test2 This is a test2
                                    this is a test3 This is a test3 this is a test3 This is a test3
                                """.trimIndent(),
                                "bob"),
                            Message("boats", "this is a test2", "lucy"),
                        ),
                        "ducks" to listOf(
                            Message("ducks", "this is a test1", "dan"),
                            Message("ducks", "this is a test2", "bob"),
                            Message("ducks", "this is a test2", "lucy"),
                        )
                    ),
                    ""
                ),
                refreshCb = {},
                onUserNavigationCb = {},
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Preview(showBackground = true)
@Composable
fun ScreenInOrderPreview() {
    UserMessagesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AllMessagesScreen(
                uiState = AllMessagesScreenViewModel.UiState.InOrder(
                    listOf(
                        Message("pets", "this is a test1", "Hubert Bonisseur de La Bath"),
                        Message("boats", "this is a test2", "Hubert Bonisseur de La Bath"),
                        Message("pets", "this is a test2", "Hubert Bonisseur de La Bath"),
                    ),
                    "",
                ),
                onUserNavigationCb = {},
                refreshCb = {},
            )
        }
    }
}