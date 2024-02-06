package fr.nabonne.usermessages.di

import fr.nabonne.usermessages.data.localstorage.LocalStoreImpl
import fr.nabonne.usermessages.data.network.RemoteApiImpl
import fr.nabonne.usermessages.domain.LocalStore
import fr.nabonne.usermessages.domain.RemoteApi

class AppModuleDefaultImpl(
) : AppModule {
    override val remoteApi: RemoteApi by lazy { RemoteApiImpl() }
    override val localStore: LocalStore by lazy { LocalStoreImpl() }
}
