package com.loyaltiez.core.data.repository

import com.loyaltiez.core.data.data_source.RetrofitInstance
import com.loyaltiez.core.domain.model.reqres.LoginResponse
import com.loyaltiez.core.domain.model.user.LoginInformation
import com.loyaltiez.core.domain.model.user.User
import com.loyaltiez.core.domain.repository.IReqResAPI
import retrofit2.Response

class ReqResAPI : IReqResAPI {

    override suspend fun loginWithEmailAndPassword(loginInformation: LoginInformation): Response<LoginResponse> {

        return RetrofitInstance.api.loginWithEmailAndPassword(loginInformation)
    }
}