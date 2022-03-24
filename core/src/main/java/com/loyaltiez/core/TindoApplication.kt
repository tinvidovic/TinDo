package com.loyaltiez.core

import android.app.Application
import com.loyaltiez.core.data.repository.ReqResAPI
import com.loyaltiez.core.di.AppContainer
import com.loyaltiez.core.domain.model.user.User
import com.loyaltiez.core.domain.repository.IReqResAPI

class TindoApplication : Application() {

    var appContainer: AppContainer? = AppContainer()

    var reqResAPI: IReqResAPI = ReqResAPI()

    var loggedInUser: User? = null
}