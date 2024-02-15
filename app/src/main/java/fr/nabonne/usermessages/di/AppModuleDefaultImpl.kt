package fr.nabonne.usermessages.di

import fr.nabonne.usermessages.common.domain.data.LocalStore
import fr.nabonne.usermessages.common.domain.data.RemoteApi
import fr.nabonne.usermessages.common.localstorage.LocalStoreImpl
import fr.nabonne.usermessages.common.network.RemoteApiImpl

class AppModuleDefaultImpl(
) : AppModule {
    override val remoteApi: RemoteApi by lazy { RemoteApiImpl() }
    override val localStore: LocalStore by lazy { LocalStoreImpl() }
}
