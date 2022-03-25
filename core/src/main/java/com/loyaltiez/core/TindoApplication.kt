package com.loyaltiez.core

import android.app.Application
import com.loyaltiez.core.data.data_source.TindoRoomDatabase
import com.loyaltiez.core.data.repository.ReqResAPI
import com.loyaltiez.core.di.AppContainer
import com.loyaltiez.core.domain.model.user.User
import com.loyaltiez.core.domain.repository.IReqResAPI
import com.loyaltiez.core.domain.repository.IToDoDAO

// The application class used for DI, different implementations of the API and DAOs can be initialized here
class TindoApplication : Application() {

    var appContainer: AppContainer? = AppContainer()

    var reqResAPI: IReqResAPI = ReqResAPI()

    lateinit var toDoDAO: IToDoDAO

    var loggedInUser: User? = null

    override fun onCreate() {
        super.onCreate()

        toDoDAO = TindoRoomDatabase.invoke(applicationContext).toDoDAO()
    }
}