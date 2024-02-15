package fr.nabonne.usermessages.common.domain.di

import fr.nabonne.usermessages.common.domain.data.LocalStore
import fr.nabonne.usermessages.common.domain.data.RemoteApi
import fr.nabonne.usermessages.common.domain.usecases.ComposeMessageUseCaseImpl
import fr.nabonne.usermessages.common.domain.usecases.GetAllMessagesUseCaseImpl
import fr.nabonne.usermessages.common.domain.usecases.GetMessagesForAuthorUseCaseImpl

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