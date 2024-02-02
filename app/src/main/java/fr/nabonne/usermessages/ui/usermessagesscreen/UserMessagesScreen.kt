package fr.nabonne.usermessages.ui.usermessagesscreen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
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
import fr.nabonne.usermessages.domain.GetMessagesForAuthorUseCaseImpl
import fr.nabonne.usermessages.domain.model.Message
import fr.nabonne.usermessages.ui.theme.UserMessagesTheme


@Composable
fun UserMessagesScreen(
    modifier: Modifier = Modifier,
    userProp: String,
    onComposerNavigationCb: (String) -> Unit,
) {
    //TODO proper DI
    val backStackEntryViewModel = viewModel(
        initializer = {
            UserMessagesScreenViewModel(
                user = userProp,
                usecase = GetMessagesForAuthorUseCaseImpl(
                    remoteApi = MainActivity.remoteApi,
                    localStore = MainActivity.localStore
                ),
//                savedStateHandle = this.createSavedStateHandle(),
            )
        }
    )

    val refreshCb: () -> Unit = remember {
        { backStackEntryViewModel.refresh() }
    }
    val uiState by backStackEntryViewModel.state.collectAsStateWithLifecycle(emptyList())
    Scaffold {

    }
    UserMessagesScreen(
        items = uiState,
        refreshCb = refreshCb,
    )
}

@Composable
fun UserMessagesScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    items: List<Message>,
    refreshCb: () -> Unit,
) {
    Column {
        Button(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            onClick = refreshCb,
        ) {
            Text("Refresh Messages")
        }
        LazyColumn {
            items(items) {
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
                        text = "${it.content}"
                    )
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
fun UserMessagesScreenPreview() {
    UserMessagesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            UserMessagesScreen(
                items = listOf(
                    Message("pets", "this is a test1", "Hubert Bonisseur de La Bath"),
                    Message("boats", "this is a test2", "Hubert Bonisseur de La Bath"),
                    Message("pets", "this is a test2", "Hubert Bonisseur de La Bath"),
                    Message("pets", "this is a test1", "Hubert Bonisseur de La Bath"),
                    Message("trains", "this is a test1", "Hubert Bonisseur de La Bath"),
                    Message("pets", "this is a test1", "Hubert Bonisseur de La Bath"),
                    Message("pets", "this is a test1", "Hubert Bonisseur de La Bath"),
                    Message("ducks", "this is a test1", "Hubert Bonisseur de La Bath"),
                    Message("pets", "this is a test1", "Hubert Bonisseur de La Bath"),
                    Message("pets", "this is a test1", "Hubert Bonisseur de La Bath"),
                    Message("planes", "this is a test1", "Hubert Bonisseur de La Bath"),
                    Message("pets", "this is a test1", "Hubert Bonisseur de La Bath"),
                    Message("pets", "this is a test1", "Hubert Bonisseur de La Bath"),
                    ),
            ) {
                Log.d("UserMessagesScreenPreview", "stub refreshCb fired" )
            }
        }
    }
}