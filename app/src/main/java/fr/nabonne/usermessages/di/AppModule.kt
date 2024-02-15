package fr.nabonne.usermessages.di

import fr.nabonne.usermessages.common.domain.data.LocalStore
import fr.nabonne.usermessages.common.domain.data.RemoteApi

interface AppModule {
    val remoteApi: RemoteApi
    val localStore: LocalStore
}