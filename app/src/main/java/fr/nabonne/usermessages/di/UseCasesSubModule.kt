package fr.nabonne.usermessages.di

import fr.nabonne.usermessages.domain.ComposeMessageUseCase
import fr.nabonne.usermessages.domain.GetAllMessagesUseCase
import fr.nabonne.usermessages.domain.GetMessagesForAuthorUseCase

interface UseCasesSubModule {
    fun injectComposeMessageUseCase(): ComposeMessageUseCase
    fun injectGetAllMessagesUseCase(): GetAllMessagesUseCase
    fun injectGetMessagesForAuthorUseCase(): GetMessagesForAuthorUseCase
}