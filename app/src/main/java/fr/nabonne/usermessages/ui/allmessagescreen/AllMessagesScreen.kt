package fr.nabonne.usermessages.ui.allmessagescreen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.nabonne.usermessages.MainActivity
import fr.nabonne.usermessages.domain.GetAllMessagesUseCaseImpl
import fr.nabonne.usermessages.domain.model.Message
import fr.nabonne.usermessages.ui.theme.UserMessagesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID


@Composable
fun AllMessagesScreen(
    modifier: Modifier = Modifier
) {
    //TODO get ViewModel from ViewModelStoreOwner through lifecycle-viewmodel-compose
    //TODO proper DI
    val getAllMessagesViewModel = AllMessagesScreenViewModel(
        GetAllMessagesUseCaseImpl(
            remoteApi = MainActivity.remoteApi,
            localStore = MainActivity.localStore
        )
    )

    val refreshCb: () -> Unit = remember {
        { getAllMessagesViewModel.refresh() }
    }
    //TODO use collectAsStateWithLifecycle
    val uiState by getAllMessagesViewModel.state.collectAsState()
    AllMessagesScreen(
        uiState = uiState,
        refreshCb = refreshCb,
    )
}

@Composable
fun AllMessagesScreen(
    modifier: Modifier = Modifier,
    uiState: AllMessagesScreenViewModel.UiState,
    refreshCb: () -> Unit,
) {
    Column {
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
                LazyColumnByAuthor(map = uiState.map)
            }
            is AllMessagesScreenViewModel.UiState.BySubject -> {
                LazyColumnBySubject(map = uiState.map)
            }
            is AllMessagesScreenViewModel.UiState.InOrder -> {
                LazyColumnInOrder(messages = uiState.list )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnByAuthor(
    modifier: Modifier = Modifier.fillMaxWidth(),
    //TODO use snapshotMap
    map: Map<String, List<Message>>
) {
    LazyColumn(modifier = modifier) {
        map.forEach {
            stickyHeader() {
                ElevatedButton(
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = {  }
                ) {
                    Text(
                        text = it.key,
                    )
                }
            }
            items(it.value) {
                ElevatedCard(
                    modifier = modifier
                        .padding(start = 24.dp, bottom = 16.dp)
                )
                {
                    Text(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        text = "Subject: ${it.subject}"
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnBySubject(
    modifier: Modifier = Modifier.fillMaxWidth(),
    //TODO use snapshotMap
    map: Map<String, List<Message>>
) {
    LazyColumn(modifier = modifier) {
        map.forEach {
            stickyHeader() {
                ElevatedButton(
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = {  }
                ) {
                    Text(
                        text = "Subject: ${it.key}",
                    )
                }
            }
            items(it.value) {
                ElevatedCard(
                    modifier = modifier
                        .padding(start = 24.dp, bottom = 16.dp)
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
    messages: List<Message>
) {
    LazyColumn {
        items(messages) {
            Row {
                Text("From: ${it.author}")
                Column {
                    Text("Subject: ${it.subject}")
                    Text("Msg: ${it.content}")
                }
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
            )
        }
    }
}