package fr.nabonne.usermessages.di;

import android.app.Application;

class UserMessagesApp : Application() {
    companion object {
        lateinit var appModule: AppModule
        val AppModule.useCasesSubModule by lazy {
            UseCasesSubModuleImpl()
        }
    }

    override fun onCreate() {
        super.onCreate()
        //Small manual dependency injection graph
        appModule = AppModuleDefaultImpl()
    }
}
