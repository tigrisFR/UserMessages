package fr.nabonne.usermessages.ui.allmessagescreen

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import fr.nabonne.usermessages.core.domain.di.UseCasesSubModule

//need public visibility for startDestination
const val ROUTE = "allMessages"

fun NavController.navigateToAllMessages(
    navOptions: NavOptions? = null,
) {
    this.navigate("$ROUTE", navOptions)
}

fun NavGraphBuilder.allMessagesScreen(
    useCasesSubModule: UseCasesSubModule,
    onUserNavigationCb: (user: String) -> Unit,
    onComposerNavigationCb: (user: String?) -> Unit,
) {
    composable("$ROUTE") {
        AllMessagesScreen(
            allMessagesViewModel = viewModel(
                initializer = {
                    AllMessagesScreenViewModel(useCasesSubModule.injectGetAllMessagesUseCase())
                }
            ),
            onUserNavigationCb = onUserNavigationCb,
            onComposerNavigationCb = onComposerNavigationCb,
        )
    }
}