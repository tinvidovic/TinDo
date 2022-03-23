package com.loyaltiez.feature_login.domain.use_cases

import com.loyaltiez.core.domain.model.reqres.LoginResponse
import com.loyaltiez.core.domain.model.user.LoginInformation
import com.loyaltiez.core.domain.repository.IReqResAPI
import com.loyaltiez.core.util.NetworkResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginUseCase(private val reqResAPI: IReqResAPI) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(loginInformation: LoginInformation): Flow<NetworkResource<LoginResponse>> {

        return withContext(Dispatchers.IO) {
            callbackFlow {

                trySend(NetworkResource.Loading())

                val response = reqResAPI.loginWithEmailAndPassword(loginInformation)

                trySend(handleLoginResponse(response))

                awaitClose { this.cancel() }
            }
        }
    }

    private fun handleLoginResponse(response: Response<LoginResponse>): NetworkResource<LoginResponse> {

        if (response.isSuccessful) {

            response.body()?.let {
                return NetworkResource.Success(it)
            }
        }

        response.body()?.let {
            return NetworkResource.Error(response.message(), response.code(), it)
        }

        return NetworkResource.Error(response.message(), response.code())
    }
}