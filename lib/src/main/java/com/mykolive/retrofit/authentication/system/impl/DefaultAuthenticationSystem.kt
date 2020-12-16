package com.mykolive.retrofit.authentication.system.impl

import com.mykolive.retrofit.authentication.storage.Token
import com.mykolive.retrofit.authentication.storage.TokenStorage
import com.mykolive.retrofit.authentication.system.AuthenticationSystem
import com.mykolive.retrofit.authentication.system.TokenService
import okhttp3.Request

class DefaultAuthenticationSystem constructor(
    private val storage: TokenStorage,
    private val tokenService: TokenService
) : AuthenticationSystem {

    override fun initialize(token: Token): Boolean {
        return storage.save(token)
    }

    override fun isEmpty(): Boolean {
        return storage.isEmpty()
    }

    override fun injectAuthorization(request: Request): Request {
        val headers = tokenService.createAuthorizationHeaders(storage.read())
        return request.newBuilder()
            .apply {
                headers.names().forEach { header(it, headers[it]!!) }
            }.build()
    }

    override fun reauthorizeRequest(request: Request): Request? {
        val invalidAuthorization = tokenService.extractToken(request.headers()) ?: return null
        val currentAuthorization = storage.read()
        if (currentAuthorization.issueTime > invalidAuthorization.issueTime)
            return injectAuthorization(request)
        if (refreshAuthorization(currentAuthorization))
            return injectAuthorization(request)
        return null
    }

    private fun refreshAuthorization(previousToken: Token): Boolean {
        return try {
            val nextToken = tokenService.refreshToken(previousToken)
            if (nextToken == null) false
            else storage.save(nextToken)
        } catch (e: Exception) {
            false
        }
    }

}