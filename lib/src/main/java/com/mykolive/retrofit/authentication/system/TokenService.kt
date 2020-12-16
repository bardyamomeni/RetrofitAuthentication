package com.mykolive.retrofit.authentication.system

import com.mykolive.retrofit.authentication.storage.Token
import okhttp3.Headers

interface TokenService {

    fun refreshToken(previousToken: Token): Token?

    fun extractToken(headers: Headers): Token?

    fun createAuthorizationHeaders(token: Token): Headers

}