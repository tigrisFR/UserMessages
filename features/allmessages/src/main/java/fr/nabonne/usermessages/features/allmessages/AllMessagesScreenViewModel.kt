package fr.nabonne.usermessages.features.allmessages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.nabonne.usermessages.core.domain.model.Message
import fr.nabonne.usermessages.core.domain.usecases.GetAllMessagesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllMessagesScreenViewModel(
    private val usecase: GetAllMessagesUseCase,
    //TODO fetch the SaveStateHandler to restore across process death
) : ViewModel() {
    sealed class UiState {
        data class ByAuthor(
            val map: Map<String, List<Message>>,
            val errorMessage: String,
        ) : UiState()
        data class BySubject(
            val map: Map<String, List<Message>>,
            val errorMessage: String,
        ) : UiState()

        data class InOrder(
            val list: List<Message>,
            val errorMessage: String,
        ) : UiState()
    }

    private val _state: MutableStateFlow<UiState> = MutableStateFlow(
        UiState.ByAuthor(
            emptyMap(),
            ""
        )
    )
    val state = _state.asStateFlow()

    init {
        allMessagesByAuthor()
    }

    private var currentJob: Job? = null

    //TODO: Error handling on our various jobs
    fun allMessagesByAuthor() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            usecase.getAllMessagesByAuthor().collect {
                _state.value = UiState.ByAuthor(it, "")
            }
            usecase.refresh()
        }
    }

    fun allMessagesBySubject() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            usecase.getAllMessagesBySubject().collect {
                _state.value = UiState.BySubject(it, "")
            }
            usecase.refresh()
        }
    }

    fun allMessagesInOrder() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            usecase.getAllMessagesInOrder().collect {
                _state.value = UiState.InOrder(it, "")
            }
            usecase.refresh()
        }
    }


    fun refresh() {
        //No need to cancel refresh
        viewModelScope.launch {
            usecase.refresh()
        }
    }
}