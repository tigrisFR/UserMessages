package fr.nabonne.usermessages.core.domain.di

import fr.nabonne.usermessages.core.domain.data.LocalStore
import fr.nabonne.usermessages.core.domain.data.RemoteApi
import fr.nabonne.usermessages.core.domain.usecases.ComposeMessageUseCaseImpl
import fr.nabonne.usermessages.core.domain.usecases.GetAllMessagesUseCaseImpl
import fr.nabonne.usermessages.core.domain.usecases.GetMessagesForAuthorUseCaseImpl

class UseCasesSubModuleImpl(
    val remoteApi: RemoteApi,// = UserMessagesApp.appModule.remoteApi,
    val localStore: LocalStore,// = UserMessagesApp.appModule.localStore,
) : UseCasesSubModule {
    override fun injectComposeMessageUseCase() =
        ComposeMessageUseCaseImpl(
            remoteApi = remoteApi,
        )
    override fun injectGetAllMessagesUseCase() =
        GetAllMessagesUseCaseImpl(
            remoteApi = remoteApi,
            localStore = localStore,
        )
    override fun injectGetMessagesForAuthorUseCase() =
        GetMessagesForAuthorUseCaseImpl(
            remoteApi = remoteApi,
            localStore = localStore,
        )
}