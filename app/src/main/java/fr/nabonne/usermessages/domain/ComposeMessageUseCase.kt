package fr.nabonne.usermessages.domain

import fr.nabonne.usermessages.domain.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ComposeMessageUseCase {
    suspend fun sendMessage()
    suspend fun updateMessage(draftVersion: Message): Boolean
}

class ComposeMessageUseCaseImpl(
    private val remoteApi: RemoteApi
): ComposeMessageUseCase {
    var messageDraft: Message = Message("","", "")

    override suspend fun updateMessage(draftVersion: Message): Boolean {
        messageDraft = draftVersion
        //TODO add storing draft to local store
        return validateMessage(messageDraft)
    }

    private fun validateMessage(message: Message): Boolean {
        return message.author.isNotBlank()
                && message.subject.isNotBlank()
                && message.content.isNotBlank()
    }

    //TODO: revisit concurrency here
    override suspend fun sendMessage() {
        val finalDraft = messageDraft.copy()
        if (validateMessage(finalDraft)) {
            withContext(Dispatchers.IO) {
                remoteApi.postMessage(finalDraft)
            }
        } else {
            throw ConcurrentModificationException("Draft not valid: $finalDraft != $messageDraft")
        }
    }
}