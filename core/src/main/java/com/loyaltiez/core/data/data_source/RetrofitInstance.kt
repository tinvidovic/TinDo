package com.loyaltiez.core.data.data_source

import com.loyaltiez.core.data.data_source.Constants.BASE_URL
import com.loyaltiez.core.domain.repository.IReqResAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// A class with a companion object providing an instance of Retrofit using google's GsonConverter
class RetrofitInstance {
    companion object {

        private val retrofit by lazy {

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api: IReqResAPI by lazy {
            retrofit.create(IReqResAPI::class.java)
        }
    }
}