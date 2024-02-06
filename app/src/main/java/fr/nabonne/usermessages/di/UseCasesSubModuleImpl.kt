package fr.nabonne.usermessages.di

import fr.nabonne.usermessages.domain.ComposeMessageUseCaseImpl
import fr.nabonne.usermessages.domain.GetAllMessagesUseCaseImpl
import fr.nabonne.usermessages.domain.GetMessagesForAuthorUseCaseImpl
import fr.nabonne.usermessages.domain.LocalStore
import fr.nabonne.usermessages.domain.RemoteApi

class UseCasesSubModuleImpl(
    val remoteApi: RemoteApi = UserMessagesApp.appModule.remoteApi,
    val localStore: LocalStore = UserMessagesApp.appModule.localStore,
) : UseCasesSubModule {
    override fun injectComposeMessageUseCase() = ComposeMessageUseCaseImpl(
            remoteApi = remoteApi,
        )
    override fun injectGetAllMessagesUseCase() = GetAllMessagesUseCaseImpl(
            remoteApi = remoteApi,
            localStore = localStore,
        )
    override fun injectGetMessagesForAuthorUseCase() = GetMessagesForAuthorUseCaseImpl(
            remoteApi = remoteApi,
            localStore = localStore,
        )
}