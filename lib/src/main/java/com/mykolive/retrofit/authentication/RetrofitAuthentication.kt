package com.mykolive.retrofit.authentication

import android.content.Context
import com.mykolive.retrofit.authentication.adapter.AuthenticatedRequestCallAdapterFactory
import com.mykolive.retrofit.authentication.interception.AuthorizationInterceptor
import com.mykolive.retrofit.authentication.interception.DefaultAuthenticator
import com.mykolive.retrofit.authentication.storage.impl.SharedPreferencesAuthorizationStorage
import com.mykolive.retrofit.authentication.system.AuthenticationSystem
import com.mykolive.retrofit.authentication.system.TokenService
import com.mykolive.retrofit.authentication.system.impl.DefaultAuthenticationSystem
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class RetrofitAuthentication(private val authenticationSystem: AuthenticationSystem) {

    private var clientBuilder: OkHttpClient.Builder? = null
    private var retrofitBuilder: Retrofit.Builder? = null

    private var retrofit: Retrofit? = null

    constructor(context: Context, tokenService: TokenService) : this(
        DefaultAuthenticationSystem(
            SharedPreferencesAuthorizationStorage(context),
            tokenService
        )
    )

    fun customizeClient(func: (OkHttpClient.Builder) -> Unit): RetrofitAuthentication {
        if (clientBuilder == null)
            clientBuilder = OkHttpClient.Builder()
        func.invoke(clientBuilder!!)
        return this
    }

    fun customizeRetrofit(func: (Retrofit.Builder) -> Unit): RetrofitAuthentication {
        if (retrofitBuilder == null)
            retrofitBuilder = Retrofit.Builder()
        func.invoke(retrofitBuilder!!)
        return this
    }

    fun createRetrofit(): Retrofit {
        val client = (clientBuilder ?: OkHttpClient.Builder())
            .addNetworkInterceptor(AuthorizationInterceptor(authenticationSystem))
            .authenticator(DefaultAuthenticator(authenticationSystem))
            .build()
        retrofit = (retrofitBuilder ?: Retrofit.Builder())
            .client(client)
            .callFactory(RetrofitAuthenticationCallFactory(client))
            .addCallAdapterFactory(AuthenticatedRequestCallAdapterFactory())
            .build()
        clientBuilder = null
        retrofitBuilder = null
        return retrofit!!
    }

    fun getRetrofit(): Retrofit {
        if (retrofit == null)
            throw IllegalStateException("first you have to create retrofit using 'createRetrofit' function")
        return retrofit!!
    }

    fun getAuthenticationSystem(): AuthenticationSystem {
        return authenticationSystem
    }
}