package fr.nabonne.usermessages.common.domain.di

import fr.nabonne.usermessages.common.domain.usecases.ComposeMessageUseCase
import fr.nabonne.usermessages.common.domain.usecases.GetAllMessagesUseCase
import fr.nabonne.usermessages.common.domain.usecases.GetMessagesForAuthorUseCase

interface UseCasesSubModule {
    fun injectComposeMessageUseCase(): ComposeMessageUseCase
    fun injectGetAllMessagesUseCase(): GetAllMessagesUseCase
    fun injectGetMessagesForAuthorUseCase(): GetMessagesForAuthorUseCase
}