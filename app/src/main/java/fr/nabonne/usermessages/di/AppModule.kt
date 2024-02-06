package fr.nabonne.usermessages.di

import fr.nabonne.usermessages.domain.LocalStore
import fr.nabonne.usermessages.domain.RemoteApi

interface AppModule {
    val remoteApi: RemoteApi
    val localStore: LocalStore
}