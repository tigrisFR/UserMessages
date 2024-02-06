package fr.nabonne.usermessages.ui.usermessagesscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.nabonne.usermessages.di.UserMessagesApp
import fr.nabonne.usermessages.di.UserMessagesApp.Companion.useCasesSubModule
import fr.nabonne.usermessages.domain.GetMessagesForAuthorUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserMessagesScreenViewModel(
    val user: String,
    private val usecase: GetMessagesForAuthorUseCase = UserMessagesApp.appModule.useCasesSubModule.injectGetMessagesForAuthorUseCase(),
    //TODO fetch the SaveStateHandler to restore across process death
//    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //TODO more elaborate state than simply passing the items from data layer
    val state = usecase.getMessagesForAuthor(user).map { (_, items) ->
        items
    }

    fun refresh() {
        viewModelScope.launch {
            usecase.refreshForUser(user)
        }
    }
}