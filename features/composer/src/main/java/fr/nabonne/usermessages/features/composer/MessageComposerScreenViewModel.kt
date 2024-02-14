package fr.nabonne.usermessages.features.composer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.nabonne.usermessages.core.domain.model.Message
import fr.nabonne.usermessages.core.domain.usecases.ComposeMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class MessageComposerScreenViewModel(
    private val usecase: ComposeMessageUseCase,
    //TODO create SaveStateHandler in our init to restore across process death
//    val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    sealed class UiState {
        object READY_TO_SEND : UiState()
        object NOT_READY_TO_SEND : UiState()
        object SENDING : UiState()
        object SENT : UiState()
    }

    private val _state: MutableStateFlow<UiState> = MutableStateFlow(UiState.NOT_READY_TO_SEND)
    val state = _state.asStateFlow()

    val uiInputs = MutableStateFlow<UiInput>(
        UiInput.UpdateDraft(
            Message(
                "",
                "",
                ""
            )
        )
    )
    sealed class UiInput(open val message: Message) {
        data class UpdateDraft(override val message: Message) : UiInput(message)
        data class SendMessage(override val message: Message): UiInput(message)
    }

    init {
        viewModelScope.launch {
            uiInputs.debounce(500)
                .collect {
                    if (state.value == UiState.SENT ||
                        state.value == UiState.SENDING
                    ) {
                        //TODO: make sure we don't get stuck here
                        //too late to attempt sending nor updating
                    } else {
                        val nextStateValue = when (it) {
                            is UiInput.SendMessage -> {
                                _state.value = UiState.SENDING
                                postMessage(it.message)
                            }
                            is UiInput.UpdateDraft -> {
                                updateMessageDraft(it.message)
                            }
                        }
                        _state.value = nextStateValue
                    }
                }
        }
    }

    private suspend fun updateMessageDraft(message: Message): UiState {
        return if (usecase.updateMessageDraft(message)) {
            UiState.READY_TO_SEND
        } else
            UiState.NOT_READY_TO_SEND
    }

    private suspend fun postMessage(message: Message): UiState {
        return try {
            if (usecase.updateMessageDraft(message)) {
                usecase.sendMessage()
                UiState.SENT
            } else {
                UiState.NOT_READY_TO_SEND
            }
        } catch (exception: Exception) {
            //TODO proper error reporting and handling
            state.value// don't change it
        }
    }
}