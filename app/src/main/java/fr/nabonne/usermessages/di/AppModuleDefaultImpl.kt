package fr.nabonne.usermessages.di

import fr.nabonne.usermessages.core.domain.data.LocalStore
import fr.nabonne.usermessages.core.domain.data.RemoteApi
import fr.nabonne.usermessages.core.localstorage.LocalStoreImpl
import fr.nabonne.usermessages.core.network.RemoteApiImpl

class AppModuleDefaultImpl(
) : AppModule {
    override val remoteApi: RemoteApi by lazy { RemoteApiImpl() }
    override val localStore: LocalStore by lazy { LocalStoreImpl() }
}
