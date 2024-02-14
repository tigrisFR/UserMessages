package fr.nabonne.usermessages.features.usermessages

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import fr.nabonne.usermessages.core.domain.di.UseCasesSubModule


private const val ROUTE_PREFIX = "userMessages"

fun NavController.navigateToUserMessages(
    user: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        route = "$ROUTE_PREFIX/${user?.trim()}",
        navOptions = navOptions,
    )
}

private const val USER_ID_ARG = "user"
fun NavGraphBuilder.userMessagesScreen(
    useCasesSubModule: UseCasesSubModule,
    onComposerNavigationCb: (user: String?) -> Unit
) {
    composable("$ROUTE_PREFIX/{$USER_ID_ARG}") {
        val user = it.arguments?.getString(USER_ID_ARG)
            ?: throw IllegalArgumentException("navigation to UserMessages screen without a user")
        UserMessagesScreen(
            userMessagesScreenViewModel = viewModel(initializer = {
                UserMessagesScreenViewModel(
                    user = user,
                    usecase = useCasesSubModule.injectGetMessagesForAuthorUseCase(),
//                savedStateHandle = this.createSavedStateHandle(),
                )
            }),
            userProp = user,
            onComposerNavigationCb = onComposerNavigationCb,
        )
    }
}