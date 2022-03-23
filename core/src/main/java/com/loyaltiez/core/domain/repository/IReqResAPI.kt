package com.loyaltiez.core.domain.repository

import com.loyaltiez.core.domain.model.reqres.LoginResponse
import com.loyaltiez.core.domain.model.user.LoginInformation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IReqResAPI {

    @POST("/api/login")
    suspend fun loginWithEmailAndPassword(@Body loginInformation: LoginInformation) : Response<LoginResponse>
}