package fr.nabonne.usermessages.core.domain.di

import fr.nabonne.usermessages.core.domain.usecases.ComposeMessageUseCase
import fr.nabonne.usermessages.core.domain.usecases.GetAllMessagesUseCase
import fr.nabonne.usermessages.core.domain.usecases.GetMessagesForAuthorUseCase

interface UseCasesSubModule {
    fun injectComposeMessageUseCase(): ComposeMessageUseCase
    fun injectGetAllMessagesUseCase(): GetAllMessagesUseCase
    fun injectGetMessagesForAuthorUseCase(): GetMessagesForAuthorUseCase
}