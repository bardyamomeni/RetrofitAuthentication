package com.mykolive.retrofit.authentication.interception

import com.mykolive.retrofit.authentication.annotation.Authenticated
import com.mykolive.retrofit.authentication.system.AuthenticationSystem
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class AuthorizationInterceptor(private val authenticationSystem: AuthenticationSystem) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java) ?: return chain.proceed(request)
        val authenticated =
            invocation.method().annotations.firstOrNull { ann ->
                ann.annotationClass == Authenticated::class
            }
        if (authenticated != null)
            return chain.proceed(authenticationSystem.injectAuthorization(request))
        return chain.proceed(request)
    }
}