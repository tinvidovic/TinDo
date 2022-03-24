package com.loyaltiez.feature_home.di

import com.loyaltiez.core.data.data_source.TindoRoomDatabase
import com.loyaltiez.core.data.repository.ReqResAPI
import com.loyaltiez.core.di.AppContainer

class HomeActivityContainer(
    roomDb: TindoRoomDatabase
)  : AppContainer() {

    // The reqres.in API concrete implementation
    private val reqResAPI = ReqResAPI()
}