package fr.nabonne.usermessages.ui.messagecomposerscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.nabonne.usermessages.domain.ComposeMessageUseCase
import fr.nabonne.usermessages.domain.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class MessageComposerScreenViewModel(
    val usecase: ComposeMessageUseCase,
    //TODO create SaveStateHandler in our init to restore across process death
//    val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    companion object {

    }

    sealed class UiState {
        object READY_TO_SEND : UiState()
        object NOT_READY_TO_SEND : UiState()
        object SENDING : UiState()
        object SENT : UiState()
    }

    private val _state: MutableStateFlow<UiState> = MutableStateFlow(UiState.NOT_READY_TO_SEND)
    val state = _state.asStateFlow()

    val uiInputs = MutableStateFlow(Message("", "", ""))

    init {
        //TODO better state management
        viewModelScope.launch {
            uiInputs.debounce(500).collect {
                if (_state.value == UiState.SENDING)
                    return@collect
                _state.value = if (usecase.updateMessage(it))
                    UiState.READY_TO_SEND
                else
                    UiState.NOT_READY_TO_SEND
            }
        }
    }

    //TODO revisit concurrency and error handling
    fun postMessage(message: Message) {
        viewModelScope.launch {
            _state.value = UiState.SENDING
            try {
                if (usecase.updateMessage(message)) {
                    usecase.sendMessage()
                    _state.value = UiState.SENT
                } else {
                    _state.value = UiState.NOT_READY_TO_SEND
                }
            } catch (exception: Exception) {
                _state.value = UiState.NOT_READY_TO_SEND
            }
        }
    }
}