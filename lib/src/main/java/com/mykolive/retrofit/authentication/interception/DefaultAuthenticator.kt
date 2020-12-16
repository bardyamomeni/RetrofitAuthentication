package com.mykolive.retrofit.authentication.interception

import com.mykolive.retrofit.authentication.system.AuthenticationSystem
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class DefaultAuthenticator(
    private val authenticationSystem: AuthenticationSystem
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            try {
                val request = response.networkResponse()?.request() ?: response.request()
                return authenticationSystem.reauthorizeRequest(request)
            } catch (e: Exception) {
                return null
            }
        }
    }
}