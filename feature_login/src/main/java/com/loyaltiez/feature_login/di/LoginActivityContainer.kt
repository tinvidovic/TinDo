package com.loyaltiez.feature_login.di

import com.loyaltiez.core.data.repository.ReqResAPI
import com.loyaltiez.core.di.AppContainer
import com.loyaltiez.core.domain.repository.IReqResAPI
import com.loyaltiez.feature_login.domain.use_cases.LoginUseCase

class LoginActivityContainer(
    reqResAPI: IReqResAPI
)  : AppContainer() {

    // The reqres.in API concrete implementation
    private val reqResAPI = ReqResAPI()

    val loginUseCase = LoginUseCase(reqResAPI)
}