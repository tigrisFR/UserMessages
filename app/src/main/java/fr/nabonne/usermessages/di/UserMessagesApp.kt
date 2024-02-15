package fr.nabonne.usermessages.di;

import android.app.Application;
import fr.nabonne.usermessages.common.domain.di.UseCasesSubModuleImpl

class UserMessagesApp : Application() {
    companion object {
        lateinit var appModule: AppModule
        val AppModule.useCasesSubModule by lazy {
            UseCasesSubModuleImpl(
                appModule.remoteApi,
                appModule.localStore,
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        //Small manual dependency injection graph
        appModule = AppModuleDefaultImpl()
    }
}
