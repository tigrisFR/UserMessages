package fr.nabonne.usermessages.di

import fr.nabonne.usermessages.core.domain.data.LocalStore
import fr.nabonne.usermessages.core.domain.data.RemoteApi

interface AppModule {
    val remoteApi: RemoteApi
    val localStore: LocalStore
}