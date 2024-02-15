package fr.nabonne.usermessages.features.composer

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import fr.nabonne.usermessages.common.domain.di.UseCasesSubModule

private const val ROUTE_PREFIX = "composer"

fun NavController.navigateToComposer(
    user: String? = null,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        route = "${ROUTE_PREFIX}?${USER_ID_ARG}=${user?.trim()}",
        navOptions = navOptions,
    )
}

private const val USER_ID_ARG = "user"
fun NavGraphBuilder.messageComposerScreen(
    useCasesSubModule: UseCasesSubModule,
    onMessageSentNavigationCb: () -> Unit
) {
    composable("${ROUTE_PREFIX}?${USER_ID_ARG}={${USER_ID_ARG}}",
        arguments = listOf(navArgument("user") {
            nullable = true
        })
    ) {
        MessageComposerScreen(
            screenViewModel = viewModel(
                initializer = {
                    MessageComposerScreenViewModel(useCasesSubModule.injectComposeMessageUseCase())
                }
            ),
            userProp = it.arguments?.getString(USER_ID_ARG),

            onMessageSentNavigationCb = onMessageSentNavigationCb,
        )
    }
}